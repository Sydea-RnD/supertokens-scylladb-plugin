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

import static io.supertokens.storage.postgresql.PreparedStatementValueSetter.NO_OP_SETTER;
import static io.supertokens.storage.postgresql.ProcessState.PROCESS_STATE.CREATING_NEW_TABLE;
import static io.supertokens.storage.postgresql.ProcessState.getInstance;
import static io.supertokens.storage.postgresql.QueryExecutorTemplate.execute;
import static io.supertokens.storage.postgresql.QueryExecutorTemplate.update;
import static io.supertokens.storage.postgresql.config.Config.getConfig;
import static io.supertokens.storage.postgresql.queries.EmailPasswordQueries.getQueryToCreatePasswordResetTokenExpiryIndex;
import static io.supertokens.storage.postgresql.queries.EmailPasswordQueries.getQueryToCreatePasswordResetTokensTable;
import static io.supertokens.storage.postgresql.queries.EmailVerificationQueries.getQueryToCreateEmailVerificationTable;
import static io.supertokens.storage.postgresql.queries.EmailVerificationQueries.getQueryToCreateEmailVerificationTokenExpiryIndex;
import static io.supertokens.storage.postgresql.queries.EmailVerificationQueries.getQueryToCreateEmailVerificationTokensTable;
import static io.supertokens.storage.postgresql.queries.JWTSigningQueries.getQueryToCreateJWTSigningTable;
import static io.supertokens.storage.postgresql.queries.PasswordlessQueries.getQueryToCreateCodeCreatedAtIndex;
import static io.supertokens.storage.postgresql.queries.PasswordlessQueries.getQueryToCreateCodeDeviceIdHashIndex;
import static io.supertokens.storage.postgresql.queries.PasswordlessQueries.getQueryToCreateCodesTable;
import static io.supertokens.storage.postgresql.queries.PasswordlessQueries.getQueryToCreateDeviceEmailIndex;
import static io.supertokens.storage.postgresql.queries.PasswordlessQueries.getQueryToCreateDevicePhoneNumberIndex;
import static io.supertokens.storage.postgresql.queries.PasswordlessQueries.getQueryToCreateDevicesTable;
import static io.supertokens.storage.postgresql.queries.SessionQueries.getQueryToCreateAccessTokenSigningKeysTable;
import static io.supertokens.storage.postgresql.queries.SessionQueries.getQueryToCreateSessionInfoTable;
import static io.supertokens.storage.postgresql.queries.UserMetadataQueries.getQueryToCreateUserMetadataTable;


public class GeneralQueries {

    private static boolean doesTableExist(Start start, String tableName) {

    }

    static String getQueryToCreateUsersTable(Start start) {

    }

    static String getQueryToCreateUserPaginationIndex(Start start) {

    }

    private static String getQueryToCreateKeyValueTable(Start start) {

    }

    public static void createTablesIfNotExists(Start start) throws StorageQueryException {
        // it throws SQLException as well, but as scylla is noSQL, we dont have that
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