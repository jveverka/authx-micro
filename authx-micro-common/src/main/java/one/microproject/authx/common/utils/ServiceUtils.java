package one.microproject.authx.common.utils;

import one.microproject.authx.common.exceptions.DataProcessingException;
import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class ServiceUtils {

    private ServiceUtils() {
    }

    public static String createId(String projectId, String id) {
        return projectId + "-" + id;
    }

}
