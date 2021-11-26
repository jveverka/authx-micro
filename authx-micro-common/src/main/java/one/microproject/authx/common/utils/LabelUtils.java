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
    public static final String AUTHX_PRIMARY_KID = "authx.primary.kid";

    public static final Long CERTIFICATE_DURATION = 365*24*60*60*1000L;  //1 year
    public static final String PRIMARY_KID = "kid-001";

    public static final Map<String, String> defaults = Map.of(
            AUTHX_CERTIFICATE_DURATION, CERTIFICATE_DURATION.toString(),
            AUTHX_ACTIVE, Boolean.TRUE.toString(),
            AUTHX_LOGIN_ENABLED, Boolean.FALSE.toString(),
            AUTHX_PRIMARY_KID, PRIMARY_KID
    );

    public static Map<String, String> mergeWithDefaults(Map<String, String> labels) {
        Map<String, String> result = new HashMap<>(labels);
        for (String key: defaults.keySet()) {
            if (!result.containsKey(key)) {
                result.put(key, defaults.get(key));
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

}
