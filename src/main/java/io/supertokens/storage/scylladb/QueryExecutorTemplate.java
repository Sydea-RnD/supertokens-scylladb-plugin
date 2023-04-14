package io.supertokens.storage.scylladb;

import io.supertokens.pluginInterface.exceptions.StorageQueryException;

public interface QueryExecutorTemplate {

    static <T> T execute(Start start, String QUERY, PreparedStatementValueSetter setter, ResultSetValueExtractor<T> mapper) throws StorageQueryException {
        // gets connection through connectionPool and then executes
    }

    static <T> T execute(Connection con, String QUERY, PreparedStatementValueSetter setter, ResultSetValueExtractor<T> mapper) throws StorageQueryException {
        // prepares statement and, through the setter, executes it
    }

    static  int update(Start start, String QUERY, PreparedStatementValueSetter setter) throws StorageQueryException {
        // gets a connection through connectionPool and then updates
    }

    static int update(Connection con, String QUERY, PreparedStatementValueSetter setter) throws StorageQueryException {
        // prepares statement and, through the setter, executes it
    }
}