package io.supertokens.storage.scylladb.queries;

import io.supertokens.pluginInterface.RowMapper;
import io.supertokens.pluginInterface.emailverification.EmailVerificationTokenInfo;
import io.supertokens.pluginInterface.exceptions.StorageQueryException;
import io.supertokens.pluginInterface.exceptions.StorageTransactionLogicException;
import io.supertokens.storage.scylladb.Start;
import io.supertokens.storage.scylladb.config.Config;
import io.supertokens.storage.scylladb.utils.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.supertokens.storage.scylladb.QueryExecutorTemplate;
import io.supertokens.storage.scylladb.config.Config;
import static java.lang.System.currentTimeMillis;

public class EmailVerificationQueries {
    
    public static boolean getQueryToCreateEmailVerificationTable(Start start) {
        
        String schema = Config.getConfig(start).getTableSchema();
        String emailVerificationTable = Config.getConfig(start).getEmailVerificationTable();
        
        String createTableStmt = "CREATE TABLE IF NOT EXISTS "
            + emailVerificationTable
            + " (user_id text, "
            + "email text, "
            + "PRIMARY KEY((user_id, email)));";
        
        try {
            QueryExecutorTemplate.execute(createTableStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
        
    }
    
    public static boolean getQueryToCreateEmailVerificationTokensTable(Start start) {
        
        String schema = Config.getConfig(start).getTableSchema();
        String emailVerificationTokensTable = Config.getConfig(start).getEmailVerificationTokensTable();
        
        String createTableStmt = "CREATE TABLE IF NOT EXISTS " 
            + emailVerificationTokensTable
            + " (token_id text, "
            + "user_id text, "
            + "email text, "
            + "token_expiry BIGINT"
            + "PRIMARY KEY(token_id));";
        
        try {
            QueryExecutorTemplate.execute(createTableStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
        
    }
    
    public static boolean getQueryToCreateEmailVerificationTokenExpiryIndex(Start start) {
        String schema = Config.getConfig(start).getTableSchema();
        String tableName = Config.getConfig(start).getEmailVerificationTokensTable();
        
        String createIndex1Stmt = "CREATE INDEX "
            + Utils.getIndexNameFromTableName(schema, tableName, "tokens", "index")
            + " ON "
            + tableName
            + "(token_id);";
        
        String createIndex2Stmt = "CREATE INDEX "
            + Utils.getIndexNameFromTableName(schema, tableName, null, "user")
            + " ON "
            + tableName
            + "(user_id);";
            
        String createIndex3Stmt = "CREATE INDEX "
            + Utils.getIndexNameFromTableName(schema, tableName, null, "email")
            + " ON "
            + tableName
            + "(email);";
        
        
        try {
            QueryExecutorTemplate.execute(createIndexStmt);
        } catch (Exception e) {
            System.out.println(e.getMesssage());
            return false;
        }
        
        try {
            QueryExecutorTemplate.execute(createIndex2Stmt);
        } catch (Exception e) {
            System.out.println(e.getMesssage());
            return false;
        }
        
        try {
            QueryExecutorTemplate.execute(createIndex3Stmt);
        } catch (Exception e) {
            System.out.println(e.getMesssage());
            return false;
        }
        
        return false;
        
    }
    
    public static boolean updateUsersIsEmailVerified(Start start, String userId, String email, boolean isEmailVerified) 
        throws SQLException, StorageQueryException {
        
        String tableName = Config.getConfig(start).getEmailVerificationTable();
        String updateStmt = null;
        
        if(isEmailVerified == true) {
            updateStmt = "INSERT INTO " 
                + tableName
                + " (user_id, email) VALUES("
                + "'" + userId + "'"
                + "'" + email + "');";
        } else {
            updateStmt = "DELETE FROM " 
                + tableName
                + " WHERE user_id = "
                + "'" + userId + "'"
                + " AND email = "
                + "'" + email + "');";            
        }
        
        try {
            QueryExecutorTemplate.execute(updateStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
        
    }
    
    public static boolean deleteAllEmailVerificationTokensForUser(Start start, String usedId, String email) 
        throws SQLException, StorageQueryException {
        
        String tableName = Config.getConfig(start).getEmailVerificationTokensTable();
        
        String deleteStmt = "DELETE FROM " 
            + tableName
            + " WHERE user_id = "
            + "'" + userId + "'"
            + " AND WHERE email = "
            + "'" + email + "';";
        
        try {
            QueryExecutorTemplate.execute(deleteStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
        
    }
    
    public static EmailVerificationTokenInfo getEmailVerificationTokenInfo(Start start, String token) 
        throws SQLException, StorageQueryException {
        
        String tableName = Config.getConfig(start).getEmailVerificationTokensTable();
        
        String selectQuery = "SELECT user_id, token_id, token_expiry, email FROM "
            + tableName
            + " WHERE token_id = "
            + "'" + token_id + "';";
            
        ResultSet rs; 
        try {
            rs = QueryExecutorTemplate.executeSelect(selectQuery);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // TODO: take data from rs and put it in EmailVerificationToken
    }
    
    public static boolean addEmailVerificationToken(Start start, String userId, String tokenHash, long expiry, String email) 
        throws SQLException, StorageQueryException {
        
        String tableName = Config.getConfig(start).getEmailVerificationTokensable();
        
        String insertStmt = "INSERT INTO " 
            + tableName 
            + " (user_id, token_id, token_expiry, email) VALUES ("
            + "'" + userId + "', "
            + "'" + tokenHash + "', "
            + "'" + (String) expiry + "', "
            + "'" + email + "');";
            
        try {
            QueryExecutorTemplate.execute(insertStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static EmailVerificationTokenInfo[] getAllEmailVerificationTokenInfoForUser(Start start, String userId, String email) 
        throws SQLException, StorageQueryException {
        
        String tableName = Config.getConfig(start).getEmailVerificationTokensTable();
        String selectQuery = "SELECT user_id, token_id, token_expiry, email FROM "
            + tableName
            + " WHERE user_id = " 
            + "'" + userId + "'"
            + " AND email = "
            + "'" + email + "'"
            + " ALLOW FILTERING;";
            
        ResultSet rs; 
        try {
            rs = QueryExecutorTemplate.executeSelect(selectQuery);
        } catch (Exception e) {
            System.out.println(e.getMessage);
        }
        // TODO: get data from rs and put it in a EmailVerificationTokenInfo[]
    }
    
    public static boolean isEmailVerified(Start start, String userId, String email) 
        throws SQLException, StorageQueryException {
        
        String tableName = Config.getConfig(start).getEmailVerificationTable();
        
        String isEmailVerifiedQuery = "SELECT * FROM "
            + tableName
            + " WHERE user_id = "
            + "'" + userId + "'"
            + " AND email = "
            + "'" + email + "';";
            
        ResultSet rs;
        try {
            rs = QueryExecutorTemplate.executeSelect(isEmailVerifiedQuery);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        // actually check if rs has a record in it, if so return true. 
        
        return true; // by default, so the check will be if rs has not one record
    }
    
    public static boolean deleteUserInfo(Start start, String userId) 
        throws SQLException, StorageQueryException {
        
        String tableName1 = Config.getConfig(start).getEmailVerificationTable();
        String tableName2 = Config.getConfig(start).getEmailVerificationTokensTable();
        
        String deleteStatementVerificationTable = "DELETE FROM "
            + tableName1
            + " WHERE user_id = "
            + "'" + userId + "';";
        
        String deleteStatementVerificationTokensTable = "DELETE FROM "
            + tableName2
            + " WHERE user_id = "
            + "'" + userId + "';";
        
        try { 
            QueryExecutorTemplate.execute(deleteStatementVerificationTable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        try {
            QueryExecutorTemplate.execute(deleteStatementVerificationTokensTable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    // TODO: complete 
    
}

