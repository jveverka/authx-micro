package one.microproject.authx.common.utils;

import one.microproject.authx.common.exceptions.DataProcessingException;

import java.util.HashMap;
import java.util.Map;

public final class LabelUtils {

    private LabelUtils() {
    }

    public static final String AUTHX_CERTIFICATE_DURATION = "authx.certificate.duration";
    public static final String AUTHX_ACTIVE = "authx.active";
    public static final String AUTHX_LOGIN_ENABLED = "authx.login.enabled";
    public static final String AUTHX_ACCESS_TOKEN_DURATION = "authx.access_token.duration";
    public static final String AUTHX_REFRESH_TOKEN_DURATION = "authx.refresh_token.duration";

    public static final Long CERTIFICATE_DURATION = 10*365*24*60*60*1000L;  //10 years
    public static final Long ACCESS_TOKEN_DURATION = 7*24*60*60*1000L;      //7 days
    public static final Long REFRESH_TOKEN_DURATION = 30*24*60*60*1000L;    //30 days

    public static final Map<String, String> defaults = Map.of(
            AUTHX_CERTIFICATE_DURATION, CERTIFICATE_DURATION.toString(),
            AUTHX_ACTIVE, Boolean.TRUE.toString(),
            AUTHX_LOGIN_ENABLED, Boolean.FALSE.toString(),
            AUTHX_ACCESS_TOKEN_DURATION, ACCESS_TOKEN_DURATION.toString(),
            AUTHX_REFRESH_TOKEN_DURATION, REFRESH_TOKEN_DURATION.toString()
    );

    public static Map<String, String> mergeWithDefaults(Map<String, String> labels) {
        Map<String, String> result = new HashMap<>(labels);
        for (Map.Entry<String, String> entry: defaults.entrySet()) {
            if (!result.containsKey(entry.getKey())) {
                result.put(entry.getKey(), defaults.get(entry.getKey()));
            }
        }
        validate(result);
        return result;
    }

    public static void validate(Map<String, String> labels) {
        try {
            if (labels.containsKey(AUTHX_CERTIFICATE_DURATION)) {
                String value = labels.get(AUTHX_CERTIFICATE_DURATION);
                Long.parseLong(value);
            }
            if (labels.containsKey(AUTHX_LOGIN_ENABLED)) {
                String value = labels.get(AUTHX_LOGIN_ENABLED);
                Boolean.valueOf(value);
            }
            if (labels.containsKey(AUTHX_ACTIVE)) {
                String value = labels.get(AUTHX_ACTIVE);
                Boolean.valueOf(value);
            }
        } catch (Exception e) {
            throw new DataProcessingException(e);
        }
    }

    public static Long getAccessTokenDuration(Long defaultValue, Map<String, String> projectLabels, Map<String, String> labels) {
        return getDuration(defaultValue, AUTHX_ACCESS_TOKEN_DURATION, projectLabels, labels);
    }

    public static Long getRefreshTokenDuration(Long defaultValue, Map<String, String> projectLabels, Map<String, String> labels) {
        return getDuration(defaultValue, AUTHX_REFRESH_TOKEN_DURATION, projectLabels, labels);
    }

    public static Long getDuration(Long defaultValue, String key, Map<String, String> projectLabels, Map<String, String> labels) {
        if (projectLabels.containsKey(key)) {
            try {
                return Long.parseLong(projectLabels.get(key));
            } catch(Exception e) {
                return defaultValue;
            }
        }
        if (labels.containsKey(key)) {
            try {
                return Long.parseLong(labels.get(key));
            } catch(Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

}
