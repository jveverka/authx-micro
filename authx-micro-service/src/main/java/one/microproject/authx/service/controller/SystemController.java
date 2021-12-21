package one.microproject.authx.service.controller;

import one.microproject.authx.common.dto.AuthxDto;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.AuthxInfo;
import one.microproject.authx.service.service.AuthXService;
import one.microproject.authx.service.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Public endpoint
 */
@RestController
@RequestMapping(path = "/api/v1/system")
public class SystemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemController.class);

    private final AuthXService authXService;
    private final ProjectService projectService;

    @Autowired
    public SystemController(AuthXService authXService, ProjectService projectService) {
        this.authXService = authXService;
        this.projectService = projectService;
    }

    @GetMapping("/info")
    public ResponseEntity<AuthxInfo> getAuthxInfo() {
        LOGGER.info("getAuthxInfo");
        Optional<AuthxDto> authxOptional = authXService.getAuthxInfo();
        if (authxOptional.isPresent()) {
            AuthxDto authx = authxOptional.get();
            List<String> projectIds = projectService.getAll().stream().map(ProjectDto::id).collect(Collectors.toList());
            AuthxInfo authxInfo = new AuthxInfo(authx.id(), projectIds);
            return ResponseEntity.ok(authxInfo);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
