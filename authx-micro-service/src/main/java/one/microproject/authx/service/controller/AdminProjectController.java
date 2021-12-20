package one.microproject.authx.service.controller;

import one.microproject.authx.common.dto.ProjectReportDto;
import one.microproject.authx.common.dto.UpdateProjectRequest;
import one.microproject.authx.service.service.AdminProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Only Global Admins and Admins for this project are authorized.
 */
@RestController
@RequestMapping(path = "/api/v1/admin/project/projects")
public class AdminProjectController {

    private final AdminProjectService adminService;

    @Autowired
    public AdminProjectController(AdminProjectService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/{project-id}")
    public ResponseEntity<ProjectReportDto> get(@PathVariable("project-id") String projectId) {
        return ResponseEntity.of(adminService.getProjectReport(projectId));
    }

    @PostMapping("/{project-id}")
    public ResponseEntity<Void> update(@RequestBody UpdateProjectRequest updateProjectRequest) {
        adminService.update(updateProjectRequest);
        return ResponseEntity.ok().build();
    }

}
