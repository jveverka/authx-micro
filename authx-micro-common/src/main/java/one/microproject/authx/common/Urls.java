package one.microproject.authx.common;

public final class Urls {

    private Urls() {
    }

    public static final String DELIMITER = "/";

    public static final String SERVICES_OAUTH2 = "/api/v1/oauth2";
    public static final String SERVICES_SYSTEM = "/api/v1/system";
    public static final String SERVICES_ADMIN_AUTHX = "/api/v1/admin/authx";
    public static final String SERVICES_ADMIN_PROJECTS = "/api/v1/admin/project/projects";
    public static final String SERVICES_ADMIN_PROJECT_CLIENTS = "/api/v1/admin/project/clients";
    public static final String SERVICES_ADMIN_PROJECT_GROUPS = "/api/v1/admin/project/groups";
    public static final String SERVICES_ADMIN_PROJECT_PERMISSIONS = "/api/v1/admin/project/permissions";
    public static final String SERVICES_ADMIN_PROJECT_ROLES = "/api/v1/admin/project/roles";
    public static final String SERVICES_ADMIN_PROJECT_USERS = "/api/v1/admin/project/users";

    public static final String TOKEN = "/token";
    public static final String INTROSPECT = "/introspect";
    public static final String REVOKE = "/revoke";
    public static final String OPENID_CONFIG = "/.well-known/openid-configuration";
    public static final String JWKS = "/.well-known/jwks.json";
    public static final String USERINFO = "/userinfo";

}
