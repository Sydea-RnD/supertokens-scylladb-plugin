package io.supertokens.storage.scylladb;

import io.supertokens.pluginInterface.exceptions.StorageQueryException;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;

public interface QueryExecutorTemplate {

    static ResultSet executeSelect(String QUERY) {

        CqlSession dbSession = ConnectionPool.getSession();
        ResultSet rs;

        try {
            rs = dbSession.execute(QUERY);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = null;
        }

        return rs;
    }

    static void execute(String QUERY) {

        CqlSession dbSession = ConnectionPool.getSession();

        try {
            dbSession.execute(QUERY);
        } catch (Exception e) {
            Systemp.out.println(e.getMessage());
        }

    }
}