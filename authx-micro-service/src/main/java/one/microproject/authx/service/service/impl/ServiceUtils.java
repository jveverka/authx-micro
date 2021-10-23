package one.microproject.authx.service.service.impl;

import one.microproject.authx.service.exceptions.DataProcessingException;
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

    public static String getSha512HashBase64(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.toBase64String(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new DataProcessingException(e);
        }
    }

}
