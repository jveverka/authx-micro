package one.microproject.authx.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static one.microproject.authx.common.Urls.SERVICES_ADMIN_PROJECT_USERS;

/**
 * Only Global Admins and Admins for this project are authorized.
 */
@RestController
@RequestMapping(path = SERVICES_ADMIN_PROJECT_USERS)
public class AdminUserController {
    //TODO:
    //List users on project
    //Create user for project
    //Delete user from project
    //Update user on project
    //Change user's credentials
    //Rotate user's keys, remove unused keys
}
