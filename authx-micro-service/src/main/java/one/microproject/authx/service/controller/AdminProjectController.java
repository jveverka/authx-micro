package one.microproject.authx.service.controller;

import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.ProjectReportDto;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.common.dto.UpdateProjectRequest;
import one.microproject.authx.service.service.AdminProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<ResponseMessage> update(@RequestBody UpdateProjectRequest updateProjectRequest) {
        ResponseMessage responseMessage = adminService.update(updateProjectRequest);
        if (responseMessage.success()) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return new ResponseEntity<>(responseMessage, HttpStatus.CONFLICT);
        }
    }

}
