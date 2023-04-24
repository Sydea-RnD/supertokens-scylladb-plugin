package io.supertokens.storage.scylladb;

import io.supertokens.pluginInterface.exceptions.QuitProgramFromPluginException;
import io.supertokens.storage.scylladb.config.Config;
import io.supertokens.storage.scylladb.config.ScyllaDBConfig;
import io.supertokens.storage.postgresql.output.Logging;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.auth.ProgrammaticPlainTextAuthProvider;
import com.datastax.oss.driver.api.core.cql.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.net.InetSocketAddress;

public class ConnectionPool extends ResourceDistributor.SingletonResource {

    private static final String RESOURCE_KEY = "io.supertokens.storage.scylladb.ConnectionPool";
    private static final CqlSession session = null;

    private static final String NODE_1 = env.getNode1();
    private static final String NODE_2 = env.getNode2();
    private static final String NODE_3 = env.getNode3();

    private ConnectionPool(Start start) {
        if (!start.enabled) {
            throw new RuntimeException("Connection to refused"); // emulates exception thrown by Hikari
        }

        ScyllaDBConfig userConfig = Config.getConfig(start);

        ArrayList<InetSocketAddress> nodesArray = new ArrayList<>();

        try {
            nodesArray.add(new InetSocketAddress(InetAddress.getByName(NODE_1).toString().split("/")[1], PORT));
            nodesArray.add(new InetSocketAddress(InetAddress.getByName(NODE_2).toString().split("/")[1], PORT));
            nodesArray.add(new InetSocketAddress(InetAddress.getByName(NODE_3).toString().split("/")[1], PORT));
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }

        try {
            DriverConfigLoader driverConfigLoader = DriverConfigLoader.programmaticBuilder()
                    .withString(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS, "DcInferringLoadBalancingPolicy")
                    .build();

            CqlSession session = CqlSession.builder()
                    .addContactPoints(nodesArray)
                    .withLocalDatacenter("AWS_EU_WEST_1")
                    .withAuthCredentials(env.getUsername(), env.getPwd())
                    .withConfigLoader(driverConfigLoader)
                    .withKeyspace("supertokens")
                    .build();

            mySession = session;
        } catch (Exception e) {
            throw new SQLException(e.toString());
        }
    }

    private static int getTimeToWaitToInit(Start start) {
        int actualValue = 3600 * 1000;
        if (Start.isTesting) {
            Integer testValue = ConnectionPoolTestContent.getInstance(start)
                    .getValue(ConnectionPoolTestContent.TIME_TO_WAIT_TO_INIT);
            return Objects.requireNonNullElse(testValue, actualValue);
        }
        return actualValue;
    }

    private static int getRetryIntervalIfInitFails(Start start) {
        int actualValue = 10 * 1000;
        if (Start.isTesting) {
            Integer testValue = ConnectionPoolTestContent.getInstance(start)
                    .getValue(ConnectionPoolTestContent.RETRY_INTERVAL_IF_INIT_FAILS);
            return Objects.requireNonNullElse(testValue, actualValue);
        }
        return actualValue;
    }

    private static ConnectionPool getInstance(Start start) {
        return (ConnectionPool) start.getResourceDistributor().getResource(RESOURCE_KEY);
    }

    static void initPool(Start start) {
        if (getInstance(start) != null) {
            return;
        }
        if (Thread.currentThread() != start.mainThread) {
            throw new QuitProgramFromPluginException("Should not come here");
        }
        Logging.info(start, "Setting up PostgreSQL connection pool.", true); // change to scylladb?
        boolean longMessagePrinted = false;
        long maxTryTime = System.currentTimeMillis() + getTimeToWaitToInit(start);
        String errorMessage = "Error connecting to PostgreSQL instance. Please make sure that PostgreSQL is running and that "
                + "you have" + " specified the correct values for ('postgresql_host' and 'postgresql_port') or for "
                + "'postgresql_connection_uri'"; // gotta change the error message
        try {
            while (true) {
                try {
                    start.getResourceDistributor().setResource(RESOURCE_KEY, new ConnectionPool(start));
                    break;
                } catch (Exception e) {
                    if (e.getMessage().contains("Connection to") && e.getMessage().contains("refused")
                            || e.getMessage().contains("the database system is starting up")) {
                        start.handleKillSignalForWhenItHappens();
                        if (System.currentTimeMillis() > maxTryTime) {
                            throw new QuitProgramFromPluginException(errorMessage);
                        }
                        if (!longMessagePrinted) {
                            longMessagePrinted = true;
                            Logging.info(start, errorMessage, true);
                        }
                        double minsRemaining = (maxTryTime - System.currentTimeMillis()) / (1000.0 * 60);
                        NumberFormat formatter = new DecimalFormat("#0.0");
                        Logging.info(start,
                                "Trying again in a few seconds for " + formatter.format(minsRemaining) + " mins...",
                                true);
                        try {
                            if (Thread.interrupted()) {
                                throw new InterruptedException();
                            }
                            Thread.sleep(getRetryIntervalIfInitFails(start));
                        } catch (InterruptedException ex) {
                            throw new QuitProgramFromPluginException(errorMessage);
                        }
                    } else {
                        throw e;
                    }
                }
            }
        } finally {
            start.removeShutdownHook();
        }
    }

    public static Connection getSession(Start start) throws SQLException {
        if (getInstance(start) == null) {
            throw new QuitProgramFromPluginException("Please call initPool before getConnection");
        }
        if (!start.enabled) {
            throw new SQLException("Storage layer disabled");
        }
        return ConnectionPool.session();
    }

    static void close(Start start) {
        if (getInstance(start) == null) {
            return;
        }
    }
}