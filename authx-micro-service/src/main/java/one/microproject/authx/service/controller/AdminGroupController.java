package one.microproject.authx.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static one.microproject.authx.common.Urls.SERVICES_ADMIN_PROJECT_GROUPS;

/**
 * Only Global Admins and Admins for this project are authorized.
 */
@RestController
@RequestMapping(path = SERVICES_ADMIN_PROJECT_GROUPS)
public class AdminGroupController {
    //TODO:
    //List groups on project
    //Create groups for project
    //Delete groups from project (only if not used by any client or user)
    //Update groups on project
}
