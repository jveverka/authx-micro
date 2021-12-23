package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record RoleDto(@JsonProperty("id") String id,
                      @JsonProperty("projectId") String projectId,
                      @JsonProperty("description") String description,
                      @JsonProperty("permissionIds") Set<String> permissionIds) {
}
