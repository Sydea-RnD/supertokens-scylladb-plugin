package io.supertokens.storage.scylladb.queries;

import io.supertokens.pluginInterface.dashboard.DashboardSessionInfo;
import io.supertokens.pluginInterface.dashboard.DashboardUser;
import io.supertokens.pluginInterface.exceptions.StorageQueryException;

import io.supertokens.storage.scylladb.QueryExecutorTemplate;
import io.supertokens.storage.scylladb.Start;
import io.supertokens.storage.scylladb.config.Config;
import io.supertokens.storage.scylladb.utils.Utils;
import io.supertokens.storage.scylladb.ConnectionPool;
import static java.lang.System.currentTimeMillis;

import com.datastax.oss.driver.api.core.cql.*;

public class DashboardQueries {
    
    public static boolean getQueryToCreateDashboardUsersTable(Start start) {
        
        String schema = Config.getConfig(start).getTableSchema();
        String dashboardUsersTable = Config.getConfig(start).getDashboardUsersTable();
        
        final String createTableStmt = "CREATE TABLE IF NOT EXISTS " + dashboardUsersTable + " ("
            + "user_id text, "
            + "email text, "
            + "password_hash text, "
            + "time_joined BIGINT, "
            + "PRIMARY KEY(user_id);";
        
        final String createIndexStmt = "CREATE INDEX IF NOT EXISTS "
            + Utils.getIndexNameFromTableName(schema, dashboardUsersTable, null, "index")
            + " ON "
            + (schema + "." + dashboardUsersTable)
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
    
    public static boolean getQueryToCreateDashboardUserSessionsTable(Start start) {
        
        String schema = Config.getConfig(start).getTableSchema();
        String tableName = Config.getConfig(start).getDashboardSessionsTable();
        
        final String createTableStmt = "CREATE TABLE IF NOT EXISTS " + 
            + tableName
            + " ("
            + "session_id text, "
            + "time_created BIGINT, "
            + "expiry BIGINT, "
            + "PRIMARY KEY (session_id));";
        
        final String createIndexStmt = "CREATE INDEX IF NOT EXISTS "
            + Utils.getIndexNameFromTableName(schema, tableName, "user_id", "index")
            + " ON "
            + (schema + "." + dashboardUsersTable)
            + "(user_id)";
            
        final String createSecondIndexStmt = "CREATE INDEX IF NOT EXISTS "
            + Utils.getIndexNameFromTableName(schema, tableName, "expiry", "index");
            + " ON "
            + (schema + "." + dashboardUsersTable)
            + "(expiry)";
            
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
        
        try {
            QueryExecutorTemplate.execute(createSecondIndexStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static boolean createDashboardUser(Start start, String userId, String email, String passwordHash, long timeJoined)
        throws SQLException, StorageQueryException {
            
        final String insertUserStmt = "INSERT INTO "
            + Config.getConfig(start).getDashboardUsersTable()
            + " (user_id, email, password_hash, time_joined)"
            + " VALUES ("
            + "'" + userId + "', "
            + "'" + email + "', "
            + "'" + passwordHash + "', "
            + "'" + (String) timeJoined "', "
            + ");";
            
        try {
            QueryExecutorTemplate.execute(insertUserStmt);
        } catch (Excepiton e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public static boolean deleteDashboardUserWithUserId(Start start, String userId)
        throws SQLException, StorageQueryException {
            
        final String deleteUserStmt = "DELETE FROM "
            + Config.getConfig(start).getDashboardUsersTable()
            + " WHERE user_id = "
            + "'" + userId + "'"
            + ";";
        
        try {
            QueryExecutorTemplate.execute(deleteUserStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    // this method should return DashboardUser[] 
    public static ResultSet getAllDashboardUsers(Start start) throws SQLException, StorageQueryExeception {
        // ...
    }
    
    public static DashboardUser getDashboardUserById(Start start, String userId)
        throws SQLException, StorageQueryException {
        
        String selectUserStmt = "SELECT * FROM "
            + Config.getConfig(start).getDashboardUsersTable()
            + " WHERE user_id = "
            + "'" + userId + "'"
            + ";";
        
        try {
            QueryExecutorTemplate.executeSelect(selectUserStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        
        // TODO: take data and put it in a DashboardUser object
    }
    
    public static boolean updateDashboardUsersEmailWithUserId(Start start, String userId, String newEmail) 
        throws SQLException, StorageQueryException {
  
        String updateEmailStmt = "UPDATE " + Config.getConfig(start).getDashboardUsersTable()
              + " SET email = "
              + "'" + newEmail + "'"
              + "' WHERE user_id = '"
              + "'" + userId + "'"
              + ";";
        
        try {
            QueryExecutorTemplate.execute(updateEmailStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static boolean updateDashboardUsersPasswordWithUserId(Start start, String userId, String newPwdHash) 
        throws SQLException, StorageQueryException {
  
        String updateEmailStmt = "UPDATE " + Config.getConfig(start).getDashboardUsersTable()
              + " SET password_hash = "
              + "'" + newPwdHash + "'"
              + "' WHERE user_id = "
              + "'" + userId + "'"
              + ";";
        
        try {
            QueryExecutorTemplate.execute(updateEmailStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static boolean createDashboardSession(Start start, String userId, String sessionId, long timeCreated, long expiry) 
        throws SQLException, StorageQueryException {
        
        String createDashboardSessionStmt = "INSERT INTO "
            + Config.getConfig(start).getDashboardSessionTable()
            + " (user_id, session_id, time_created, expiry) "
            + "VALUES ("
            + "'" + userId + "', "
            + "'" + sessionId + "', "
            + (String) timeCreated + ", "
            + (String) expiry
            + ");";
        
        try {
            QueryExecutorTemplate.execute(createDashboardSessionStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
    }
    
    public static DasboardSessionInfo getSessionInfoWithSessionId(Start start, String sessionId) 
        throws SWLException, StorageQueryException {
        
        String getSessionInfoStmt = "SELECT * FROM "
            + Config.getConfig(start).getDashboardSessionsTable()
            + " WHERE session_id = "
            + "'" + sessionId + "', "
            + ";";
            
        
        try {
            QueryExecutorTemplate.executeSelect(getSessionInfoStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        // TODO: take the data out the record and put it in a DashboardSessionInfo object
    }
    
    public static DashboardSessionInfo[] getAllSessionsForUserId(Start start, String userId) 
        throw SQLException, StorageQueryException {
        
        String getAllSessionsStmt = "SELECT * FROM "
            + Config.getConfig(start).getDashboardSessionsTable()
            + " WHERE user_id = "
            + "'" + userId + "', "
            + ";";
        
        try {
            QueryExecutorTemplate.executeSelect(getAllSessionsStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        // TODO: take the data out the record and put it in a DashboardSessionInfo[] object
        
    }

    public static boolean deleteExpiredSessions(Start start) 
            throws SQLException, StorageQueryException {
            
        long currentTimeMillis = System.currentTimeMillis();
        String deleteExpiredSessionsStmt = "DELETE FROM "
            + Config.getConfig(start).getDashboardSessionsTable()
            + " WHERE expiry < "
            + (String) currentTimeMillis
            + ";";
            // + " ALLOW FILTERING;";
                
        try {
            QueryExecutorTemplate.execute(deleteExpiredSessionsStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
            
        return true;
    }
            
    public static DashboardUser getDashboarUserByEmail(Start start, String email) 
        throws SQLException, StorageQueryException {
        
        Stirng getDashboardUserStmt = "SELECT * FROM "
            + Config.getConfig(start).getDashboardUsersTable()
            + " WHERE email = '"
            + email
            + "';";
        
        try {
            QueryExecutorTemplate.executeSelect(getDashboardUserStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // TODO: take the data out the record and put it in a DashboardSessionInfo[] object
        
    }

    public static boolean deleteDashboardUserSessionWithSessionId(Start start, String sessionId) 
        throws SQLException, StorageQueryException {
        
        String deleteDashboardStmt = "DELETE FROM "
            + Config.getConfig(start).getDashboardSessionsTable()
            + " WHERE session_id = '"
            + sessionId 
            + "';";
            
        try {
            QueryExecutorTemplate.execute(deleteDashboardStmt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
                        
}