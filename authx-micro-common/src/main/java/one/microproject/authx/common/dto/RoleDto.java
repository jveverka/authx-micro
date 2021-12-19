package one.microproject.authx.common.dto;

import java.util.Set;

public record RoleDto(String id, String projectId, String description, Set<String> roleIds) {
}
