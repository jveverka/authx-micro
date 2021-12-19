package one.microproject.authx.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/admin/permissions")
public class AdminPermissionController {
    //TODO:
    //List permission on project
    //Create permission for project
    //Delete permission from project (only if not used by any role)
    //Update permission on project
}
