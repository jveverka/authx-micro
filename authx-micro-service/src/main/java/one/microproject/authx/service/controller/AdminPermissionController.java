package one.microproject.authx.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Only Global Admins and Admins for this project are authorized.
 */
@RestController
@RequestMapping(path = "/api/v1/admin/project/permissions")
public class AdminPermissionController {
    //TODO:
    //List permission on project
    //Create permission for project
    //Delete permission from project (only if not used by any role)
    //Update permission on project
}
