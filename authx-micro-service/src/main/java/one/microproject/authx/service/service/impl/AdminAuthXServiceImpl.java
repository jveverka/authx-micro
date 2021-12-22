package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.AuthxDto;
import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.service.service.AdminAuthXService;
import one.microproject.authx.service.service.AuthXService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AdminAuthXServiceImpl implements AdminAuthXService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminAuthXServiceImpl.class);

    private final AuthXService authXService;
    private final ProjectService projectService;
    private final UserService userService;
    private final ClientService clientService;
    private final GroupService groupService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public AdminAuthXServiceImpl(AuthXService authXService, ProjectService projectService, UserService userService, ClientService clientService,
                                 GroupService groupService, RoleService roleService, PermissionService permissionService) {
        this.authXService = authXService;
        this.projectService = projectService;
        this.userService = userService;
        this.clientService = clientService;
        this.groupService = groupService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @Override
    public List<ProjectDto> getAll() {
        return projectService.getAll();
    }

    @Override
    @Transactional
    public ResponseMessage buildProject(BuildProjectRequest buildProjectRequest) {
        String projectId = buildProjectRequest.createProjectRequest().id();
        LOGGER.info("Building project id={}", projectId);
        Optional<ProjectDto> projectDtoOptional = projectService.get(projectId);
        if (projectDtoOptional.isPresent()) {
            return ResponseMessage.error("Project id=" + projectId + " already exists.");
        }
        projectService.create(buildProjectRequest.createProjectRequest());
        return ResponseMessage.ok("Project id=" + projectId + " created.");
    }

    @Override
    @Transactional
    public ResponseMessage deleteRecursively(String projectId) {
        LOGGER.info("Delete project id={} recursively !", projectId);
        Optional<AuthxDto> authxDtoOptional = authXService.getAuthxInfo();
        if (authxDtoOptional.isPresent()) {
            AuthxDto authxDto = authxDtoOptional.get();
            List<String> adminProjects = authxDto.globalAdminProjectIds();
            adminProjects.remove(projectId);
            if (adminProjects.isEmpty()) {
                return ResponseMessage.error("Can't delete last global admin project id=" + projectId + ".");
            } else {
                authXService.createOrUpdate(new AuthxDto(authxDto.id(), adminProjects));
            }
        } else {
            return ResponseMessage.error("Database is not populated !");
        }
        Optional<ProjectDto> projectDtoOptional = projectService.get(projectId);
        if (projectDtoOptional.isEmpty()) {
            return ResponseMessage.error("Project id=" + projectId + " not found.");
        } else {
            projectService.remove(projectId);
            userService.removeAll(projectId);
            clientService.removeAll(projectId);
            permissionService.removeAll(projectId);
            roleService.removeAll(projectId);
            groupService.removeAll(projectId);
            return ResponseMessage.ok("Project id=" + projectId + " deleted.");
        }
    }

    @Override
    public boolean isGlobalAdminProject(String projectId) {
        Optional<AuthxDto> authxInfo = authXService.getAuthxInfo();
        if (authxInfo.isPresent()) {
            return authxInfo.get().globalAdminProjectIds().stream().anyMatch(p -> p.equals(projectId));
        }
        return false;
    }

    @Override
    public boolean isAdminUser(String projectId, String userId) {
        return projectService.isAdmin(projectId, userId);
    }

}
