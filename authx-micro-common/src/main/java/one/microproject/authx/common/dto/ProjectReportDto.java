package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProjectReportDto(@JsonProperty("project") ProjectDto project,
                               @JsonProperty("groups") List<GroupDto> groups,
                               @JsonProperty("clients") List<ClientDto> clients,
                               @JsonProperty("users") List<UserDto> users,
                               @JsonProperty("roles") List<RoleDto> roles,
                               @JsonProperty("permissions") List<PermissionDto> permissions) {
}
