package one.microproject.authx.common.utils;

public final class ServiceUtils {

    private ServiceUtils() {
    }

    public static String createId(String projectId, String id) {
        return projectId + "-" + id;
    }

}
