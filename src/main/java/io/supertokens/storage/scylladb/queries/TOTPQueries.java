package io.supertokens.storage.scylladb.queries;

import com.datastax.oss.driver.api.core.cql.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;

import io.supertokens.storage.scylladb.QueryExecutorTemplate;

import io.supertokens.storage.scylladb.Start;
import io.supertokens.storage.scylladb.config.Config;
import io.supertokens.pluginInterface.exceptions.StorageQueryException;
import io.supertokens.pluginInterface.exceptions.StorageTransactionLogicException;
import io.supertokens.pluginInterface.totp.TOTPDevice;
import io.supertokens.pluginInterface.totp.TOTPUsedCode;

public class TOTPQueries {
    
}