package io.supertokens.storage.scylladb.queries;

import io.supertokens.pluginInterface.KeyValueInfo;
import io.supertokens.pluginInterface.RECIPE_ID;
import io.supertokens.pluginInterface.RowMapper;
import io.supertokens.pluginInterface.authRecipe.AuthRecipeUserInfo;
import io.supertokens.pluginInterface.dashboard.DashboardSearchTags;
import io.supertokens.pluginInterface.exceptions.StorageQueryException;

import io.supertokens.storage.scylladb.ConnectionPool;
import io.supertokens.storage.scylladb.Start;
import io.supertokens.storage.scylladb.config.Config;
import io.supertokens.storage.scylladb.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.supertokens.storage.scylladb.PreparedStatementValueSetter.NO_OP_SETTER;
import static io.supertokens.storage.scylladb.ProcessState.PROCESS_STATE.CREATING_NEW_TABLE;
import static io.supertokens.storage.scylladb.QueryExecutorTemplate.execute;
import static io.supertokens.storage.scylladb.ProcessState.getInstance;
import static io.supertokens.storage.scylladb.QueryExecutorTemplate.update;
import static io.supertokens.storage.scylladb.config.Config.getConfig;
import static io.supertokens.storage.scylladb.queries.EmailPasswordQueries.getQueryToCreatePasswordResetTokenExpiryIndex;
import static io.supertokens.storage.scylladb.queries.EmailPasswordQueries.getQueryToCreatePasswordResetTokensTable;
import static io.supertokens.storage.scylladb.queries.EmailVerificationQueries.getQueryToCreateEmailVerificationTable;
import static io.supertokens.storage.scylladb.queries.EmailVerificationQueries.getQueryToCreateEmailVerificationTokenExpiryIndex;
import static io.supertokens.storage.scylladb.queries.EmailVerificationQueries.getQueryToCreateEmailVerificationTokensTable;
import static io.supertokens.storage.scylladb.queries.JWTSigningQueries.getQueryToCreateJWTSigningTable;
import static io.supertokens.storage.scylladb.queries.PasswordlessQueries.getQueryToCreateCodeCreatedAtIndex;
import static io.supertokens.storage.scylladb.queries.PasswordlessQueries.getQueryToCreateCodeDeviceIdHashIndex;
import static io.supertokens.storage.scylladb.queries.PasswordlessQueries.getQueryToCreateCodesTable;
import static io.supertokens.storage.scylladb.queries.PasswordlessQueries.getQueryToCreateDeviceEmailIndex;
import static io.supertokens.storage.scylladb.queries.PasswordlessQueries.getQueryToCreateDevicePhoneNumberIndex;
import static io.supertokens.storage.scylladb.queries.PasswordlessQueries.getQueryToCreateDevicesTable;
import static io.supertokens.storage.scylladb.queries.SessionQueries.getQueryToCreateAccessTokenSigningKeysTable;
import static io.supertokens.storage.scylladb.queries.SessionQueries.getQueryToCreateSessionInfoTable;
import static io.supertokens.storage.scylladb.queries.UserMetadataQueries.getQueryToCreateUserMetadataTable;


public class GeneralQueries {

    private static boolean doesTableExist(Start start, String tableName) {
        try {
            String QUERY = "SELECT 1 FROM " + tableName + " LIMIT 1";
            // execute query and return true or false
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    static String getQueryToCreateUsersTable(Start start) {
        String keyspace = Config.getConfig(start).getKeyspace();
        String usersTable = Config.getConfig(start).getUsersTable();

        return "CREATE TABLE IF NOT EXISTS " + usersTable + " ("
                + "user_id TEXT NOT NULL "
                + "recipe_id VARCHAR NOT NULL "
                + "time_joined BIGINT NOT NULL "
                + "PRIMARY KEY (user_id));";
    }

    static String getQueryToCreateUserPaginationIndex(Start start) {
        // TODO: create statement
    }

    private static String getQueryToCreateKeyValueTable(Start start) {
        String keyspace = Config.getConfig(start).getKeyspace();
        String keyValueTable = Config.getConfig(start).getKeyValueTable();

        return "CREATE TABLE IF NOT EXISTS " + keyValueTable + " ("
                + "name VARCHAR "
                + "value TEXT "
                + "created_at_time BIGINT "
                + "PRIMARY KEY (name)"
                + " );";
    }

    public static void createTablesIfNotExists(Start start) throws StorageQueryException {

        int numberOfRetries = 0;
        boolean retry = true;

        while(retry) {
            retry = false;

            if(!doesTableExist(start, Config.getConfig(start).getKeyValueTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if (!doesTableExist(start, Config.getConfig(start).getUsersTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getUsersTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getUserLastActiveTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getAccessTokenSigningKeysTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getSessionInfoTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getEmailPasswordUsersTable())) {
                 getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getPasswordResetTokensTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getEmailVerificationTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getEmailVerificationsTokenTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getThirdPartyUsersTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getJWTSigningKeysTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getPasswordlessUsersTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getPasswordlessDevicesTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getPasswordlessCodesTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getUserMetadataTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getUserMetadataTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getUserRolesPermissionTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getUserRolesTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getUserIdMapping())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getDashboardUsersTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getDashboardSessionsTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getTotpUsersTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).geTotpUserDevicesTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }

            if(!doesTableExist(start, Config.getConfig(start).getTotpUsedCodesTable())) {
                getInstance(start).addState(CREATING_NEW_TABLE, null);
                // execute query
            }
        }
    }

    @TestOnly
    public static void deleteAllTables(Start start) throws StorageQueryException {
        // it throws SQLException as well, but as scylla is noSQL, we dont have that
    }

    public static void setKeyValue_Transaction(Start start, Connection con, String key, KeyValueInfo info) throws StorageQueryException {
        // it throws SQLException as well, but as scylla is noSQL, we dont have that
    }

    public static void setKeyValue(Start start, String key, KeyValueInfo info) throws StorageQueryException {
        // it throws SQLException as well, but as scylla is noSQL, we dont have that
    }

    public static void KeyValueInfo(Start start, String key) throws StorageQueryException {
        // it throws SQLException as well, but as scylla is noSQL, we dont have that
    }

    public static void deleteKeyValue_Transaction(Start start, Connection con, String key) throws StorageQueryException {
        // it throws SQLException as well, but as scylla is noSQL, we dont have that
    }

    public static long getUsersCount(Start start, RECIPE_ID[] includeRecipeIds) throws StorageQueryException {
        // it throws SQLException as well, but as scylla is noSQL, we dont have that
    }

    public static boolean doesUserIdExist(Start start, String userId) throws StorageQueryException {
        // it throws SQLException as well, but as scylla is noSQL, we dont have that
    }

    public static AuthRecipeUserInfo[] getUsers(Start start, @NotNull Integer limit, @NotNull String timeJoinedOrder,
                                                @Nullable RECIPE_ID[] includeRecipeIds, @Nullable String userId, @Nullable Long timeJoined,
                                                @Nullable DashboardSearchTags dashboardSearchTags) throws StorageQueryException {
        // it throws SQLException as well, but as scylla is noSQL, we dont have that
    }

    private static List<? extends AuthRecipeUserInfo> getUserInfoForRecipeIdFromUserIds(Start start, RECIPE_ID recipeId,
                                                                                        List<String> userIds) throws StorageQueryException {
        // it throws SQLException as well, but as scylla is noSQL, we dont have that
    }

    private static class UserInfoPaginationResultHolder {

    }

    private static class KeyValueInfoRowMapper implements RowMapper<KeyValueInfo, ResultSet> {

    }

}