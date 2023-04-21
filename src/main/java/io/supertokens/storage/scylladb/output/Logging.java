package io.supertokens.storage.scylladb.output;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import io.supertokens.pluginInterface.LOG_LEVEL;
import io.supertokens.storage.scylladb.ResourceDistributor;
import io.supertokens.storage.scylladb.Start;
import io.supertokens.storage.scylladb.config.Config;
import io.supertokens.storage.scylladb.utils.Utils;
import org.slf4j.LoggerFactory;

public class Logging extends ResourceDistributor.SingletonResource {

    private static final String RESOURCE_ID = "io.supertokens.storage.scylladb.output.Logging";
    private final Logger infoLogger;
    private final Logger errorLogger;

    private Logging(Start start, String infoLogPath, String errorLogPath) {
        this.infoLogger = infoLogPath.equals("null")
                ? createLoggerForConsole(start, "io.supertokens.storage.scylladb.Info")
                : createLoggerForFile(start, infoLogPath, "io.supertokens.storage.scylladb.Info");
        this.errorLogger = errorLogPath.equals("null")
                ? createLoggerForConsole(start, "io.supertokens.storage.scylladb.Error")
                : createLoggerForFile(start, errorLogPath, "io.supertokens.storage.scylladb.Error");
    }

    private static Logging getInstance(Start start) {
        return (Logging) start.getResourceDistributor().getResource(RESOURCE_ID);
    }

    public static void initFileLogging(Start start, String infoLogPath, String errorLogPath) {
        if (getInstance(start) == null) {
            start.getResourceDistributor().setResource(RESOURCE_ID, new Logging(start, infoLogPath, errorLogPath));
        }
    }

    public static void debug(Start start, String msg) {
        if (!Config.getLogLevels(start).contains(LOG_LEVEL.DEBUG)) {
            return;
        }
        try {
            msg = msg.trim();
            if (getInstance(start) != null) {
                getInstance(start).infoLogger.debug(msg);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void info(Start start, String msg, boolean toConsoleAsWell) {
        if (!Config.getLogLevels(start).contains(LOG_LEVEL.INFO)) {
            return;
        }
        try {
            msg = msg.trim();
            if (getInstance(start) != null) {
                getInstance(start).infoLogger.info(msg);
            }
            if (toConsoleAsWell) {
                systemOut(msg);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void warn(Start start, String msg) {
        if (!Config.getLogLevels(start).contains(LOG_LEVEL.WARN)) {
            return;
        }
        try {
            msg = msg.trim();
            if (getInstance(start) != null) {
                getInstance(start).errorLogger.warn(msg);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void error(Start start, String err, boolean toConsoleAsWell) {
        try {
            if (!Config.getLogLevels(start).contains(LOG_LEVEL.ERROR)) {
                return;
            }
        } catch (Throwable ignored) {
            // if it comes here, it means that the config was not loaded and that we are trying
            // to log some other error. In this case, we want to log it anyway, so we catch any
            // error and continue below.
        }
        try {
            err = err.trim();
            if (getInstance(start) != null) {
                getInstance(start).errorLogger.error(err);
            }
            if (toConsoleAsWell || getInstance(start) == null) {
                systemErr(err);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void error(Start start, String message, boolean toConsoleAsWell, Exception e) {
        try {
            if (!Config.getLogLevels(start).contains(LOG_LEVEL.ERROR)) {
                return;
            }
        } catch (Throwable ignored) {
            // if it comes here, it means that the config was not loaded and that we are trying
            // to log some other error. In this case, we want to log it anyway, so we catch any
            // error and continue below.
        }
        try {
            String err = Utils.exceptionStacktraceToString(e).trim();
            if (getInstance(start) != null) {
                getInstance(start).errorLogger.error(err);
            } else {
                systemErr(err);
            }
            if (message != null) {
                message = message.trim();
                if (getInstance(start) != null) {
                    getInstance(start).errorLogger.error(message);
                }
                if (toConsoleAsWell || getInstance(start) == null) {
                    systemErr(message);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    private static void systemOut(String msg) {
        if (!Start.silent) {
            System.out.println(msg);
        }
    }

    private static void systemErr(String err) {
        System.err.println(err);
    }

    public static void stopLogging(Start start) {
        if (getInstance(start) == null) {
            return;
        }
        getInstance(start).infoLogger.detachAndStopAllAppenders();
        getInstance(start).errorLogger.detachAndStopAllAppenders();
    }

    private Logger createLoggerForFile(Start start, String file, String name) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        LayoutWrappingEncoder ple = new LayoutWrappingEncoder(start.getProcessId());
        ple.setContext(lc);
        ple.start();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile(file);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(name);
        logger.addAppender(fileAppender);
        logger.setAdditive(false); /* set to true if root should log too */

        return logger;
    }

    private Logger createLoggerForConsole(Start start, String name) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        LayoutWrappingEncoder ple = new LayoutWrappingEncoder(start.getProcessId());
        ple.setContext(lc);
        ple.start();
        ConsoleAppender<ILoggingEvent> logConsoleAppender = new ConsoleAppender<>();
        logConsoleAppender.setEncoder(ple);
        logConsoleAppender.setContext(lc);
        logConsoleAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(name);
        logger.addAppender(logConsoleAppender);
        logger.setAdditive(false); /* set to true if root should log too */

        return logger;
    }

}