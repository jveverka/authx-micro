package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.ClientDto;
import one.microproject.authx.common.dto.GroupDto;
import one.microproject.authx.common.dto.PermissionDto;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.ProjectReportDto;
import one.microproject.authx.common.dto.RoleDto;
import one.microproject.authx.common.dto.UpdateProjectRequest;
import one.microproject.authx.common.dto.UserDto;
import one.microproject.authx.service.service.AdminProjectService;
import one.microproject.authx.service.service.ClientService;
import one.microproject.authx.service.service.GroupService;
import one.microproject.authx.service.service.PermissionService;
import one.microproject.authx.service.service.ProjectService;
import one.microproject.authx.service.service.RoleService;
import one.microproject.authx.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminProjectServiceImpl implements AdminProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminProjectServiceImpl.class);

    private final ProjectService projectService;
    private final UserService userService;
    private final ClientService clientService;
    private final GroupService groupService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public AdminProjectServiceImpl(ProjectService projectService, UserService userService, ClientService clientService,
                                   GroupService groupService, RoleService roleService, PermissionService permissionService) {
        this.projectService = projectService;
        this.userService = userService;
        this.clientService = clientService;
        this.groupService = groupService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @Override
    public Optional<ProjectReportDto> getProjectReport(String projectId) {
        Optional<ProjectDto> optionalProjectDto = projectService.get(projectId);
        if (optionalProjectDto.isEmpty()) {
            return Optional.empty();
        } else {
            List<GroupDto> groups = groupService.getAll(projectId);
            List<ClientDto> clients = clientService.getAll(projectId);
            List<UserDto> users = userService.getAll(projectId);
            List<RoleDto> roles = roleService.getAll(projectId);
            List<PermissionDto> permissions = permissionService.getAll(projectId);
            return Optional.of(new ProjectReportDto(optionalProjectDto.get(), groups, clients, users, roles, permissions));
        }
    }

    @Override
    public void update(UpdateProjectRequest updateProjectRequest) {
        projectService.update(updateProjectRequest);
    }

}
