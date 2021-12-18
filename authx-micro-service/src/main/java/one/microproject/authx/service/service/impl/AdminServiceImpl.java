package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.service.service.AdminService;
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

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final ProjectService projectService;
    private final UserService userService;
    private final ClientService clientService;
    private final GroupService groupService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public AdminServiceImpl(ProjectService projectService, UserService userService, ClientService clientService,
                            GroupService groupService, RoleService roleService, PermissionService permissionService) {
        this.projectService = projectService;
        this.userService = userService;
        this.clientService = clientService;
        this.groupService = groupService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @Override
    public ResponseMessage buildProject(BuildProjectRequest buildProjectRequest) {
        return null;
    }

    @Override
    public ResponseMessage deleteRecursively(String projectId) {
        return null;
    }

}
