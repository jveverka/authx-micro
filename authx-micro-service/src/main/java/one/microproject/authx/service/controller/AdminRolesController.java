package one.microproject.authx.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static one.microproject.authx.common.Urls.SERVICES_ADMIN_PROJECT_ROLES;

/**
 * Only Global Admins and Admins for this project are authorized.
 */
@RestController
@RequestMapping(path = SERVICES_ADMIN_PROJECT_ROLES)
public class AdminRolesController {
    //List role on project
    //Create role for project
    //Delete role from project (only if not used by any client or user)
    //Update role on project
}
