package one.microproject.authx.service.controller;

import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.service.service.AdminService;
import one.microproject.authx.service.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/management/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final AdminService adminService;

    @Autowired
    public ProjectController(ProjectService projectService, AdminService adminService) {
        this.projectService = projectService;
        this.adminService = adminService;
    }

    @GetMapping()
    ResponseEntity<List<ProjectDto>> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    @GetMapping("/{project-id}")
    ResponseEntity<ProjectDto> get(@PathVariable("project-id") String projectId) {
        return ResponseEntity.of(projectService.get(projectId));
    }

    @PostMapping("/build")
    ResponseEntity<ResponseMessage> buildProject(BuildProjectRequest buildProjectRequest) {
        return ResponseEntity.ok(adminService.buildProject(buildProjectRequest));
    }

    @DeleteMapping("/{project-id}")
    ResponseEntity<ResponseMessage> deleteRecursively(@PathVariable("project-id") String projectId) {
        return ResponseEntity.ok(adminService.deleteRecursively(projectId));
    }

}
