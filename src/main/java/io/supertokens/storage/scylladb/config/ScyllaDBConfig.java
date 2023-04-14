package io.supertokens.storage.scylladb.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.supertokens.pluginInterface.exceptions.QuitProgramFromPluginException;

import java.net.URI;

// ScyllaDBConfig needs to have a method that returns an ArrayList<InetSocketAddress> which can be used
// to create a CqlSession in ConnectionPool.java

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScyllaDBConfig {

    @JsonProperty
    private boolean isHosted = false;

    @JsonProperty
    private String[] scylladb_nodes = null;

    @JsonProperty
    private String scylladb_hosted_zone = null;

    @JsonProperty
    private String scylladb_user = null;

    @JsonProperty
    private String scylladb_password = null;

    @JsonProperty
    private String scylladb_region = null;

    @JsonProperty
    private int scylladb_payload_max_size = null;

    @JsonProperty
    private String scylladb_key_value_table_name = null;

    @JsonProperty
    private String scylladb_session_info_table_name = null;

    @JsonProperty
    private String scylladb_emailpassword_users_table_name = null;

    @JsonProperty
    private String scylladb_emailverification_tokens_table_name = null;

    @JsonProperty
    private String scylladb_emailverification_verified_emails_table_name = null;

    @JsonProperty
    private String scylladb_emailpassword_pswd_reset_tokens_table_name = null;

    @JsonProperty
    private String scylladb_thirdparty_users_table_name = null;

    @JsonProperty
    private String scylladb_table_names_prefix = null;

    @JsonProperty
    private int scylladb_db_parallelism = null;

    @JsonProperty
    private int scylladb_parallel_files = null;

    @JsonProperty
    private String scylladb_host = null;

    @JsonProperty
    private int scylladb_port = null;

    /*
    public int getConnectionPoolSize() {
        return postgresql_connection_pool_size;
    }
    */

    // TODO: modify this function to work with scylla nodes (maybe it is not even useful to us.)
    public String getConnectionAttributes() {
        if (postgresql_connection_uri != null) {
            URI uri = URI.create(postgresql_connection_uri);
            String query = uri.getQuery();
            if (query != null) {
                if (query.contains("allowPublicKeyRetrieval=")) {
                    return query;
                } else {
                    return query + "&allowPublicKeyRetrieval=true";
                }
            }
        }
        return "allowPublicKeyRetrieval=true";
    }

    public String getHostName() {

        if (this.scylladb_host == null) {
            return "Error"; // TODO: improve error handling
        }
        return this.scylladb_host;
    }

    public ArrayList<InetSocketAddress> getScyllaNodes() {

        ArrayList<InetSocketAddress> scyllaNodes = new ArrayList<>();
        try {
            for (String node : this.scylladb_nodes) {
                // split for :
                // get url, port
                String[] splittedNode = node.split(":");
                String nodeUrl = splittedNode[0];
                String nodePort = splittedNode[1];

                scyllaNodes.add(new InetSocketAddress(InetAddress.getByName(nodeUrl).toString().split("/")[1], nodePort.toInteger()));
            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }

    }

    public String getUser() {
        if (this.scylladb_user != null) {
            return this.scylladb_user;
        }else {
            return "Error"; // TODO: improve error handling
        }
    }

    public String getPassword() {
        if (this.scylladb_password != null) {
            return this.scylladb_password;
        }else {
            return "Error"; // TODO: improve error handling
        }
    }

    // maybe not useful?
    /*
    public String getDatabaseName() {
        if (postgresql_database_name == null) {
            if (postgresql_connection_uri != null) {
                URI uri = URI.create(postgresql_connection_uri);
                String path = uri.getPath();
                if (path != null && !path.equals("") && !path.equals("/")) {
                    if (path.startsWith("/")) {
                        return path.substring(1);
                    }
                    return path;
                }
            }
            return "supertokens";
        }
        return postgresql_database_name;
    }
    */

    public String getConnectionURI() {
        return postgresql_connection_uri;
    }

    public String getUsersTable() {
        return addSchemaAndPrefixToTableName("all_auth_recipe_users");
    }

    public String getKeyValueTable() {
        String tableName = "key_value";
        if (postgresql_key_value_table_name != null) {
            return addSchemaToTableName(postgresql_key_value_table_name);
        }
        return addSchemaAndPrefixToTableName(tableName);
    }

    public String getUserLastActiveTable() {
        return addSchemaAndPrefixToTableName("user_last_active");
    }

    public String getAccessTokenSigningKeysTable() {
        return addSchemaAndPrefixToTableName("session_access_token_signing_keys");
    }

    public String getSessionInfoTable() {
        String tableName = "session_info";
        if (postgresql_session_info_table_name != null) {
            return addSchemaToTableName(postgresql_session_info_table_name);
        }
        return addSchemaAndPrefixToTableName(tableName);
    }

    public String getEmailPasswordUsersTable() {
        String tableName = "emailpassword_users";
        if (postgresql_emailpassword_users_table_name != null) {
            return addSchemaToTableName(postgresql_emailpassword_users_table_name);
        }
        return addSchemaAndPrefixToTableName(tableName);
    }

    public String getPasswordResetTokensTable() {
        String tableName = "emailpassword_pswd_reset_tokens";
        if (scylladb_emailpassword_pswd_reset_tokens_table_name != null) {
            return addSchemaToTableName(scylladb_emailpassword_pswd_reset_tokens_table_name);
        }
        return addSchemaAndPrefixToTableName(tableName);
    }

    public String getEmailVerificationTokensTable() {
        String tableName = "emailverification_tokens";
        if (postgresql_emailverification_tokens_table_name != null) {
            return addSchemaToTableName(postgresql_emailverification_tokens_table_name);
        }
        return addSchemaAndPrefixToTableName(tableName);
    }

    public String getEmailVerificationTable() {
        String tableName = "emailverification_verified_emails";
        if (postgresql_emailverification_verified_emails_table_name != null) {
            return addSchemaToTableName(postgresql_emailverification_verified_emails_table_name);
        }
        return addSchemaAndPrefixToTableName(tableName);
    }

    public String getThirdPartyUsersTable() {
        String tableName = "thirdparty_users";
        if (postgresql_thirdparty_users_table_name != null) {
            return addSchemaToTableName(postgresql_thirdparty_users_table_name);
        }
        return addSchemaAndPrefixToTableName(tableName);
    }

    public String getPasswordlessUsersTable() {
        String tableName = "passwordless_users";
        return addSchemaAndPrefixToTableName(tableName);
    }

    public String getPasswordlessDevicesTable() {
        String tableName = "passwordless_devices";
        return addSchemaAndPrefixToTableName(tableName);
    }

    public String getPasswordlessCodesTable() {
        String tableName = "passwordless_codes";
        return addSchemaAndPrefixToTableName(tableName);
    }

    public String getJWTSigningKeysTable() {
        return addSchemaAndPrefixToTableName("jwt_signing_keys");
    }

    public String getUserMetadataTable() {
        return addSchemaAndPrefixToTableName("user_metadata");
    }

    public String getRolesTable() {
        return addSchemaAndPrefixToTableName("roles");
    }

    public String getUserRolesPermissionsTable() {
        return addSchemaAndPrefixToTableName("role_permissions");
    }

    public String getUserRolesTable() {
        return addSchemaAndPrefixToTableName("user_roles");
    }

    public String getUserIdMappingTable() {
        return addSchemaAndPrefixToTableName("userid_mapping");
    }

    public String getDashboardUsersTable() {
        return addSchemaAndPrefixToTableName("dashboard_users");
    }

    public String getDashboardSessionsTable() {
        return addSchemaAndPrefixToTableName("dashboard_user_sessions");
    }

    public String getTotpUsersTable() {
        return addSchemaAndPrefixToTableName("totp_users");
    }

    public String getTotpUserDevicesTable() {
        return addSchemaAndPrefixToTableName("totp_user_devices");
    }

    public String getTotpUsedCodesTable() {
        return addSchemaAndPrefixToTableName("totp_used_codes");
    }

    private String addSchemaAndPrefixToTableName(String tableName) {
        String name = tableName;
        if (!postgresql_table_names_prefix.trim().equals("")) {
            name = postgresql_table_names_prefix.trim() + "_" + name;
        }
        return addSchemaToTableName(name);
    }

    /*
    private String addSchemaToTableName(String tableName) {
        String name = tableName;
        if (!postgresql_table_schema.trim().equals("public")) {
            name = postgresql_table_schema.trim() + "." + name;
        }
        return name;
    }
    */

    void validateAndInitialise() {
        if (postgresql_connection_uri != null) {
            try {
                URI ignored = URI.create(postgresql_connection_uri);
            } catch (Exception e) {
                throw new QuitProgramFromPluginException(
                        "The provided postgresql connection URI has an incorrect format. Please use a format like "
                                + "postgresql://[user[:[password]]@]host[:port][/dbname][?attr1=val1&attr2=val2...");
            }
        } else {
            if (this.getUser() == null) {
                throw new QuitProgramFromPluginException(
                        "'postgresql_user' and 'postgresql_connection_uri' are not set. Please set at least one of "
                                + "these values");
            }
        }

        if (getConnectionPoolSize() <= 0) {
            throw new QuitProgramFromPluginException(
                    "'postgresql_connection_pool_size' in the config.yaml file must be > 0");
        }
    }

}