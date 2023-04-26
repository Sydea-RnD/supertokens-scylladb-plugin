package io.supertokens.storage.scylladb.queries;

import io.supertokens.pluginInterface.emailpassword.PasswordResetTokenInfo;
import io.supertokens.pluginInterface.emailpassword.UserInfo;
import io.supertokens.pluginInterface.exceptions.StorageQueryException;
import io.supertokens.pluginInterface.exceptions.StorageTransactionLogicException;
import io.supertokens.storage.scylladb.Start;
import io.supertokens.storage.scylladb.config.Config;
import io.supertokens.storage.scylladb.utils.Utils;

import static io.supertokens.pluginInterface.RECIPE_ID.EMAIL_PASSWORD;
import static io.supertokens.storage.postgresql.PreparedStatementValueSetter.NO_OP_SETTER;
import static io.supertokens.storage.postgresql.QueryExecutorTemplate;
import static io.supertokens.storage.postgresql.config.Config;
import static java.lang.System.currentTimeMillis;

public class EmailPasswordQueries {
    
    public static boolean getQueryToCreateUsersTable(Start start) {
        
        String schema = Config.getConfig(start).getTableSchema();
        String emailPasswordUsersTable = Config.getConfig(start).getEmailPasswordUsersTable();
        
        final String createTableStmt = "CREATE TABLE IF NOT EXISTS "
            + emailPasswordUsersTable
            + " (user_id text, " 
            + "email text, "
            + "password_hash text, "
            + "time_joined BIGINT, "
            + "PRIMARY KEY (user_id));";
        
        final String createIndexStmt = "CREATE INDEX IF NOT EXISTS "
            + Utils.getIndexNameFromTableName(schema, emailPasswordUsersTable, null, "index")
            + " ON "
            + (schema + "." + emailPasswordUsersTable)
            + "(email)";
            
        try {
            QueryExecutorTemplate.execute(createTableStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
            
        try {
            QueryExecutorTemplate.execute(createIndexStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
        
    }
    
    public static boolean getQueryToCreatePasswordResetTokensTable(Start start) {
        
        String schema = Config.getConfig(start).getTableSchema();
        String passwordResetTokensTable = Config.getConfig(start).getPasswordResetTokensTable();
        
        final String createTableStmt = "CREATE TABLE IF NOT EXISTS "
            + passwordResetTokensTable
            + "(userid_token text, "
            + "user_id text, "
            + "token_id text, "
            + "token_expiry BIGINT, "
            + "PRIMARY KEY(userid_token));"
        
        final String createFirstIndex = "CREATE INDEX IF NOT EXISTS "
            + Utils.getIndexNameFromTableName(schema, passwordResetTokensTable, "user", "index")
            + " ON "
            + passwordResetTokensTable
            + "(user_id);";
            
        final String createSecondIndex = "CREATE INDEX IF NOT EXISTS "
            + Utils.getIndexNameFromTableName(schema, passwordResetTokensTable, "token", "index")
            + " ON "
            + passwordResetTokensTable
            + "(token_id);";
        
        final String createThirdIndex = "CREATE INDEX IF NOT EXISTS "
            + Utils.getIndexNameFromTableName(schema, passwordResetTokensTable, "expiry", "index")
            + " ON "
            + passwordResetTokensTable
            + "(token_expiry);";
        
        try {
            QueryExecutorTemplate.execute(createTableStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        try {
            QueryExecutorTemplate.execute(createFirstIndex);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        try {
            QueryExecutorTemplate.execute(createSecondIndex);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        try {
            QueryExecutorTemplate.execute(createThirdIndex);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
        
    }
    
    public static boolean deleteExpiredPasswordResetTokens(Start start) {
        
        String passwordResetTokensTable = Config.getConfig(start).getPasswordResetTokensTable();
        long currentTimeMillis = System.currentTimeMillis();
        
        final String deleteTableStmt = "DELETE FROM "
            + passwordResetTokensTable 
            + " WHERE token_expiry < "
            + (String) currentTimeMillis
            + " ALLOW FILTERING;";
        
        try {
            QueryExecutorTemplate.execute(deleteExpiredTokens);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static boolean updateUsersEmail(Start start, String userId, String newEmail) 
        throws SQLException, StorageQueryException {
        String tableName = Config.getConfig(start).getEmailPasswordUsersTable();
        
        String updateStmt = "UPDATE " 
            + tableName 
            + " SET email = "
            + "'" + newEmail + "'"
            + " WHERE user_id = "
            + "'" + userId + "';";
        
        try {
            QueryExecutorTemplate.execute(updateStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static boolean updateUsersPassword(Start start, String userId, String newPassword) 
        throws SQLException, StorageQueryException {
        String tableName = Config.getConfig(start).getEmailPasswordUsersTable();
        
        final String updateStmt = "UPDATE " 
            + tableName 
            + " SET password_hash = "
            + "'" + newPassword + "'"
            + " WHERE user_id = "
            + "'" + userId + "';";
        
        try {
            QueryExecutorTemplate.execute(updateStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static boolean deleteAllPasswordResetTokensForUser(Start start, String userId) 
        throws SQLException, StorageQueryException {
        
        String tableName = Config.getConfig(start).getPasswordResetTokensTable();
        
        final String deleteStmt = "DELETE FROM "
            + tableName 
            + " WHERE user_id = "
            + "'" + userId + "';";
        
        try {
            QueryExecutorTemplate.execute(deleteStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
        
    }
    
    public static PasswordResetTokenInfo[] getAllPasswordResetTokenInfoForUser(Start start, String userId) 
        throws SQLException, StorageQueryException {
            
        final String tableName = Config.getConfig(start).getPasswordResetTokensTable();
        final String queryStmt = "SELECT user_id, token_id, token_expiry FROM "
            + tableName
            + " WHERE user_id = "
            + "'" + userId + "';";
        
        ResultSet rs;        
        try {
            rs = QueryExecutorTemplate.executeTemplate(queryStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        // TODO: get the data from rs and put it in a PasswordResetTokenInfo[]
        
    }
    
    public static UserInfo getUserInfoUsingId(Start start, String userId) 
        throws SQLException, StorageQueryException {
        
        String tableName = Config.getConfig(start).getEmailPasswordUsersTable();
        String queryStmt = "" // query set as untranslatable, will have to find a work around
        
    }
    
    public static PasswordResetTokenInfo getPasswordResetTokenInfo(Start start, String tokenId) 
        throws SQLException, StorageQueryException {
        
        String tableName = Config.getConfig(start).getPasswordResetTokensTable(); 
        final String queryStmt = "SELECT user_id, token_id, token_expiry FROM "
            + tableName
            + " WHERE token_id = "
            + "'" + tokenId + "';";
        
        ResultSet rs;
        
        try {
            rs = QueryExecutorTemplate.executeStatement(queryStmt);    
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        // TODO: take the data from rs and return it in a PasswordResetTokenInfo object
    }
    
    public static boolean addPasswordResetToken(Start start, String userId, String tokenHash, long expiry) 
        throws SQLException, StorageQueryException {
        
        String tableName = Config.getConfig(start).getPasswordResetTokensTable();
        final String queryStmt = "INSERT INTO "
            + tableName 
            + "(user_id, token_id, token_expiry) VALUES ("
            + "'" + userId + "',"
            + "'" + tokenHash + "',"
            + "'" + (String) expiry + "');";
            
        try {
            QueryExecutorTemplate.execute(queryStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
        
    }
    
    public static boolean signUp(Start start, String userId, String email, String passwordHash, long timeJoined) 
        throws StorageQueryException {
        
        final String usersTableName = Config.getConfig(start).getUsersTable();
        final String emailPasswordUsersTableName = Config.getConfig(start).getEmailPasswordUsersTable();
        
        final String insertIntoUsersTable = "INSERT INTO "
            + usersTableName 
            + " (user_id, recipe_id, time_joined) VALUES ("
            + "'" + userId + "',"
            + "'" + EMAIL_PASSWORD.toString() + "',"
            + "'" + (String) timeJoined + "',";
        
        final String insertIntoEmailPasswordUsersTable = "INSERT INTO "
            + emailPasswordUsersTableName 
            + "(user_id, email, password_hash, time_joined) VALUES ("
            + "'" + userId + "',"
            + "'" + email + "',"
            + "'" + passwordHash + "',"
            + "'" + (String) timeJoined + "';";
            
        try { 
            QueryExecutorTemplate.execute(insertIntoUsersTable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        try { 
            QueryExecutorTemplate.execute(insertIntoEmailPasswordUsersTable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return false;
        
    }
    
    public static boolean deleteUser(Start start, String userId) 
        throws StorageQueryException {
        
        final String usersTableName = Config.getConfig(start).getUsersTable();
        final String emailPasswordUsersTableName = Config.getConfig(start).getEmailPasswordUsersTable();
        
        final String deleteFromUsersTableStmt = "DELETE FROM "
            + usersTableName 
            + " WHERE user_id = "
            + "'" + userId + "'"
            + " AND recipe_id = "
            + "'" + EMAIL_PASSWORD.toString() + "';";
            
        final String deleteFromEmailPasswordUsersTableStmt = "DELETE FROM "
            + emailPasswordUsersTableName
            + " WHERE user_id = "
            + "'" + userId + "';";
            
        try {
            QueryExecutorTemplate.execute(deleteFromUsersTableStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        try {
            QueryExecutorTemplate.execute(deleteFromEmailPasswordUsersTableStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
        
    }
    
    
    
}
