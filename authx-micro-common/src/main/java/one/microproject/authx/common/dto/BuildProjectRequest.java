package one.microproject.authx.common.dto;

import java.util.List;

public record BuildProjectRequest(CreateProjectRequest createProjectRequest, List<GroupDto> groups,
                                  List<RoleDto> roles, List<PermissionDto> permissions,
                                  List<CreateClientRequest> clients, List<CreateUserRequest> users) {
}
