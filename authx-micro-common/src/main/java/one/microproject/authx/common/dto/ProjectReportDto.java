package one.microproject.authx.common.dto;

import java.util.List;

public record ProjectReportDto(ProjectDto project, List<GroupDto> groups, List<ClientDto> clients, List<UserDto> users, List<RoleDto> roles, List<PermissionDto> permissions) {
}
