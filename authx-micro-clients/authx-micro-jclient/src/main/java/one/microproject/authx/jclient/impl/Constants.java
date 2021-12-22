package one.microproject.authx.jclient.impl;

public final class Constants {

    private Constants() {
    }

    public static final String DELIMITER = "/";
    public static final String SERVICES_OAUTH2 = "/api/v1/oauth2";
    public static final String SERVICES_SYSTEM = "/api/v1/system";
    public static final String SERVICES_ADMIN_AUTHX = "/api/v1/admin/authx";
    public static final String SCOPE = "&scope=";
    public static final String CLIENT_ID = "&client_id=";
    public static final String CLIENT_SECRET = "&client_secret=";

    public static final String TOKEN = "/token";
    public static final String INTROSPECT = "/introspect";
    public static final String REVOKE = "/revoke";

}
