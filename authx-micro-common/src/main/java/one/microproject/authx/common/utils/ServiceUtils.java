package one.microproject.authx.common.utils;

import one.microproject.authx.common.dto.PermissionDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ServiceUtils {

    private ServiceUtils() {
    }

    public static String createId(String projectId, String id) {
        return projectId + "-" + id;
    }

    public static Set<String> getScopes(Set<String> requestedScopes, Set<PermissionDto> permissions) {
        Set<String> result = new HashSet<>();
        if (permissions != null) {
            permissions.forEach(p -> {
                String permission = p.resource() + "." + p.service() + "." + p.action();
                result.add(permission);
            });
        }
        if (requestedScopes != null) {
            if (!requestedScopes.isEmpty()) {
                //TODO: add scope filtering
            }
        }
        return result;
    }

    public static Set<String> getScopes(List<PermissionDto> permissions) {
        Set<String> result = new HashSet<>();
        if (permissions != null) {
            permissions.forEach(p -> {
                String permission = p.resource() + "." + p.service() + "." + p.action();
                result.add(permission);
            });
        }
        return result;
    }

}
