package one.microproject.authx.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.service.service.AdminAuthXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Only Global Admins are authorized.
 */
@RestController
@RequestMapping(path = "/api/v1/admin/authx")
@Tag(name = "Admin Authx APIs", description = "APIs providing base Authx admin capabilities.")
public class AdminAuthXController {

    private final AdminAuthXService adminAuthXService;

    @Autowired
    public AdminAuthXController(AdminAuthXService adminAuthXService) {
        this.adminAuthXService = adminAuthXService;
    }

    @GetMapping()
    public ResponseEntity<List<ProjectDto>> getAll() {
        return ResponseEntity.ok(adminAuthXService.getAll());
    }


    @Operation(description = "__Build complete project__ - create new project with all associated data structures like clients, users, roles, groups and permissions.")
    @PutMapping("/projects/build")
    public ResponseEntity<ResponseMessage> buildProject(@RequestBody BuildProjectRequest buildProjectRequest) {
        ResponseMessage responseMessage = adminAuthXService.buildProject(buildProjectRequest);
        if (responseMessage.success()) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return new ResponseEntity<>(responseMessage, HttpStatus.CONFLICT);
        }
    }

    @Operation(description = "__Delete complete project__ - completely delete project with all associated data structures like clients, users, roles, groups and permissions.")
    @DeleteMapping("/projects/{project-id}")
    public ResponseEntity<ResponseMessage> deleteRecursively(@PathVariable("project-id") String projectId) {
        ResponseMessage responseMessage = adminAuthXService.deleteRecursively(projectId);
        if (responseMessage.success()) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return new ResponseEntity<>(responseMessage, HttpStatus.CONFLICT);
        }
    }

}
