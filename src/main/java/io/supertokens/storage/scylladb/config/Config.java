package io.supertokens.storage.scylladb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.supertokens.pluginInterface.LOG_LEVEL;
import io.supertokens.pluginInterface.exceptions.QuitProgramFromPluginException;
import io.supertokens.storage.scylladb.ResourceDistributor;
import io.supertokens.storage.scylladb.Start;
import io.supertokens.storage.scylladb.output.Logging;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Config extends ResourceDistributor.SingletonResource {

    private static final String RESOURCE_KEY = "io.supertokens.storage.scylladb.config.Config";
    private final ScyllaDBConfig config;
    private final Start start;
    private final Set<LOG_LEVEL> logLevels;

    private Config(Start start, String configFilePath, Set<LOG_LEVEL> logLevels) {
        this.start = start;
        this.logLevels = logLevels;
        try {
            config = loadScyllaDBConfig(configFilePath);
        } catch (IOException e) {
            throw new QuitProgramFromPluginException(e);
        }
    }

    private static Config getInstance(Start start) {
        return (Config) start.getResourceDistributor().getResource(RESOURCE_KEY);
    }

    public static void loadConfig(Start start, String configFilePath, Set<LOG_LEVEL> logLevels) {
        if (getInstance(start) != null) {
            return;
        }
        start.getResourceDistributor().setResource(RESOURCE_KEY, new Config(start, configFilePath, logLevels));
        Logging.info(start, "Loading PostgreSQL config.", true); // change message
    }

    public static ScyllaDBConfig getConfig(Start start)  throws QuitProgramFromPluginException {
        if (getInstance(start) == null) {
            throw new QuitProgramFromPluginException("Please call loadConfig() before calling getConfig()");
        }
        return getInstance(start).config;
    }

    public static Set<LOG_LEVEL> getLogLevels(Start start) {
        return getInstance(start).logLevels;
    }

    // see canBeUsed(...)
    private ScyllaDBConfig loadScyllaDBConfig(String configFilePath) throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ScyllaDBConfig config = mapper.readValue(new File(configFilePath), ScyllaDBConfig.class);
        config.validateAndInitialise();
        return config;
    }

    public static boolean canBeUsed(String configFilePath) {
        try {
            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            ScyllaDBConfig config = mapper.readValue(new File(configFilePath), ScyllaDBConfig.class);
            return config.getUser() != null || config.getPassword() != null || config.getConnectionURI() != null;
        } catch (Exception e) {
            return false;
        }
    }

}