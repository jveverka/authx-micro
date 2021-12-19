package one.microproject.authx.service.controller;

import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.oauth2.AuthxInfo;
import one.microproject.authx.service.model.Authx;
import one.microproject.authx.service.service.AuthXService;
import one.microproject.authx.service.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class StatusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusController.class);

    private final AuthXService authXService;
    private final ProjectService projectService;

    @Autowired
    public StatusController(AuthXService authXService, ProjectService projectService) {
        this.authXService = authXService;
        this.projectService = projectService;
    }

    public ResponseEntity<AuthxInfo> getAuthxInfo() {
        LOGGER.info("getAuthxInfo");
        Optional<Authx> authxOptional = authXService.getAuthxInfo();
        if (authxOptional.isPresent()) {
            Authx authx = authxOptional.get();
            List<String> projectIds = projectService.getAll().stream().map(ProjectDto::id).collect(Collectors.toList());
            AuthxInfo authxInfo = new AuthxInfo(authx.getId(), projectIds);
            return ResponseEntity.ok(authxInfo);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
