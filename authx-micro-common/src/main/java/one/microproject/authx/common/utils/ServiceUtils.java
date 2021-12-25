package one.microproject.authx.common.utils;

import one.microproject.authx.common.dto.PermissionDto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class ServiceUtils {

    private ServiceUtils() {
    }

    public static String createId(String projectId, String id) {
        return projectId + "-" + id;
    }

    public static Set<String> getScopes(Set<String> requestedScopes, Set<PermissionDto> permissions) {
        Set<String> result = getScopes(permissions);
        if (requestedScopes != null) {
            if (!requestedScopes.isEmpty()) {
                Set<String> filteredScopes = new HashSet<>();
                requestedScopes.forEach(s -> {
                    if (result.contains(s)) {
                        filteredScopes.add(s);
                    }
                });
                return filteredScopes;
            }
        }
        return result;
    }

    public static Set<String> getScopes(Collection<PermissionDto> permissions) {
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
