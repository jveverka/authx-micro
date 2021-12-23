package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BuildProjectRequest(@JsonProperty("createProjectRequest") CreateProjectRequest createProjectRequest,
                                  @JsonProperty("groups") List<GroupDto> groups,
                                  @JsonProperty("roles") List<RoleDto> roles,
                                  @JsonProperty("permissions") List<PermissionDto> permissions,
                                  @JsonProperty("clients") List<CreateClientRequest> clients,
                                  @JsonProperty("users") List<CreateUserRequest> users) {
}
