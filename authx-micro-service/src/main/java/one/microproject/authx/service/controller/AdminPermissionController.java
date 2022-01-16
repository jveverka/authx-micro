package one.microproject.authx.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static one.microproject.authx.common.Urls.SERVICES_ADMIN_PROJECT_PERMISSIONS;

/**
 * Only Global Admins and Admins for this project are authorized.
 */
@RestController
@RequestMapping(path = SERVICES_ADMIN_PROJECT_PERMISSIONS)
public class AdminPermissionController {
    //TODO:
    //List permission on project
    //Create permission for project
    //Delete permission from project (only if not used by any role)
    //Update permission on project
}
