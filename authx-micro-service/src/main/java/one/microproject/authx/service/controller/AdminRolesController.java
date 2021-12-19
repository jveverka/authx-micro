package one.microproject.authx.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/admin/roles")
public class AdminRolesController {
    //List role on project
    //Create role for project
    //Delete role from project (only if not used by any client or user)
    //Update role on project
}
