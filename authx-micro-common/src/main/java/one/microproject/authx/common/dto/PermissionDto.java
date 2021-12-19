package one.microproject.authx.common.dto;

public record PermissionDto(String id, String projectId, String description, String service, String resource, String action) {
}
