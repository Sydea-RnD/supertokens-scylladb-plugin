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
    private String scylladb_custom_keyspace = null;

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

    private final String USERS_TABLE = "all_auth_recipe_users";
    private final String KEY_VALUE_TABLE = "key_value";
    private final String USER_LAST_ACTIVE_TABLE = "user_last_active";
    private final String ACCESS_TOKEN_SIGNIN_KEYS_TABLE = "session_access_token_signin_keys";
    private final String SESSION_INFO_TABLE = "session_info";
    private final String EMAIL_PASSWORD_USERS_TABLE = "emailpassword_users";
    private final String PASSWORD_RESET_TOKENS_TABLE = "emailpassword_pswd_reset_token";
    private final String EMAIL_VERIFICATION_TOKENS_TABLE = "emailverification_tokens";
    private final String EMAIL_VERIFICATION_VERIFIED_EMAILS_TABLE = "emailverification_verified_emails";
    private final String THIRD_PARTY_USERS_TABLE = "thirdparty_users";
    private final String PASSWORDLESS_USERS_TABLE = "passwordless_users";
    private final String PASSWORDLESS_DEVICES_TABLE = "passwordless_devices";
    private final String PASSWORDLESS_CODES_TABLE = "passwordless_codes";
    private final String JWT_SIGNIN_KEYS_TABLE = "jwt_signin_keys";
    private final String USER_METADATA_TABLE = "user_metadata";
    private final String ROLES_TABLE = "roles";
    private final String USER_ROLES_PERMISSION_TABLE = "role_permission";
    private final String USER_ROLES_TABLE = "user_roles";
    private final String USER_ID_MAPPING_TABLE = "userid_mapping";
    private final String DASHBOARD_USERS_TABLE = "dashboard_users";
    private final String DASHBOARD_SESSIONS_TABLE = "dashboard_user_sessions";
    private final String TOTP_USERS_TABLE = "totp_users";
    private final String TOTP_USER_DEVICES_TABLE = "totp_user_devices";
    private final String TOTP_USED_CODES_TABLE = "totp_used_codes";

    // methods to get all table names
    public String getUsersTable() {
        return this.USERS_TABLE;
    }

    public String getKeyValueTable() {
        return this.KEY_VALUE_TABLE;
    }

    public String getUserLastActiveTable() {
        return this.USER_LAST_ACTIVE_TABLE;
    }

    public String getAccessTokenSigningKeys() {
        return this.ACCESS_TOKEN_SIGNIN_KEYS_TABLE;
    }

    public String getSessionInfoTable(){
        return this.SESSION_INFO_TABLE;
    }

    public String getKeySpace() { return this.scylladb_custom_keyspace; }

    public String getEmailPasswordUsersTable() {
        return this.EMAIL_PASSWORD_USERS_TABLE;
    }

    public String getPasswordResetTokensTable() {
        return this.PASSWORD_RESET_TOKENS_TABLE;
    }

    public String getEmailVerificationTokensTable() {
        return this.EMAIL_VERIFICATION_TOKENS_TABLE;
    }

    public String getEmailVerificationTable() {
        return this.EMAIL_VERIFICATION_VERIFIED_EMAILS_TABLE;
    }

    public String getThirdPartyUsersTable() {
        return this.THIRD_PARTY_USERS_TABLE;
    }

    public String getPasswordlessUsersTable() {
        return this.PASSWORDLESS_USERS_TABLE;
    }

    public String getPasswordlessDevicesTable() {
        return this.PASSWORDLESS_DEVICES_TABLE;
    }

    public String getPasswordlessCodesTable() {
        return this.PASSWORDLESS_CODES_TABLE;
    }

    public String getUserMetadataTable() {
        return this.USER_METADATA_TABLE;
    }

    public String getRolesTable() {
        return this.ROLES_TABLE;
    }

    public String getUserRolesPermissionTable() {
        return this.USER_ROLES_PERMISSION_TABLE;
    }

    public String getUserRolesTable() {
        return this.USER_ROLES_TABLE;
    }

    public String getUserIdMappingTable() {
        return this.USER_ID_MAPPING_TABLE;
    }

    public String getDashboardUsersTable() {
        return this.DASHBOARD_USERS_TABLE;
    }

    public String getDashboardSessionsTable() {
        return this.DASHBOARD_SESSIONS_TABLE;
    }

    public String getTotpUsersTable() {
        return this.TOTP_USERS_TABLE;
    }

    public String getTotpUserDevicesTable() {
        return this.TOTP_USER_DEVICES_TABLE;
    }

    public String getTotpUsedCodesTable() {
        return this.TOTP_USED_CODES_TABLE;
    }

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

    public String getConnectionURI() {
        return postgresql_connection_uri;
    }

    public void validateAndInitialise() throws QuitProgramFromPluginException {
        // this method has to check if all the mandatory parameters are inserted and valid

        if(this.scylladb_host == null) {
            throw new QuitProgramFromPluginException("'scylladb_host' is not set, please set it and retry");
        }

        if(this.scylladb_user == null || this.scylladb_password == null) {
            throw new QuitProgramFromPluginException("Your ScyllaDB credentials are not valid, please set them correctly and retry");
        }

        if(this.scylladb_nodes == null) {
            throw new QuitProgramFromPluginException("Your ScyllaDB nodes are not set, please set them and retry");
        }

        if(this.scylladb_hosted_zone == null) {
            throw new QuitProgramFromPluginException("Your ScyllaDB hosted zone is not set, please set it and retry");
        }

    }

}