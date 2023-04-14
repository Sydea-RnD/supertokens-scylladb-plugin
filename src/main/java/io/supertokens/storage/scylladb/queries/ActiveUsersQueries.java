package io.supertokens.storage.scylladb.queries;

import io.supertokens.storeage.scylladb.Start;
import io.supertokens.pluginInterface.exceptions.StorageQueryException;
import io.supertokens.storage.scylladb.config.Config;

import static io.supertokens.storage.scylladb.QueryExecutorTemplate.execute;
import static io.supertokens.storage.scylladb.QueryExecutorTemplate.update;

public class ActiveUsersQueries {

    public static String getQueryToCreateUserLastActiveTable(Start start) {

    }

    public static int countUsersActiveSinge(Start start, long sinceTime) {

    }

    public static int countUsersEnabledTotp(Start start) throws StorageQueryException {
        // throws SQLException as well, but scylladb is nosql
    }

    public static int countUsersEnabledTotpAndActiveSince(Start start, long sinceTime) throws StorageQueryException {
        // throws SQLException as well, but scylladb is nosql
    }

    public static int updateUserLastActive(Start start, String userId) throws StorageQueryException {
        // throws SQLException as well, but scylladb is nosql
    }

}
