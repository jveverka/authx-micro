package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.AuthxDto;
import one.microproject.authx.common.dto.CreateProjectRequest;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.service.model.Authx;
import one.microproject.authx.service.service.AuthXService;
import one.microproject.authx.service.service.DataInitService;
import one.microproject.authx.service.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class DataInitServiceImpl implements DataInitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitServiceImpl.class);

    private final AuthxDto authxDto;
    private final CreateProjectRequest initialModel;
    private final ProjectService projectService;
    private final AuthXService authXService;

    private String globalAdminProjectId;

    @Autowired
    public DataInitServiceImpl(AuthxDto authxDto, CreateProjectRequest initialModel,
                               ProjectService projectService, AuthXService authXService) {
        this.authxDto = authxDto;
        this.initialModel = initialModel;
        this.projectService = projectService;
        this.authXService = authXService;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("INIT: Authx data model init ...");
        globalAdminProjectId = initialModel.id();
        LOGGER.info("INIT: Checking initial Authx model.");
        Optional<Authx> authxInfo = authXService.getAuthxInfo();
        if (authxInfo.isPresent()) {
            Authx authx = authxInfo.get();
            LOGGER.info("INIT: Using Authx model {} {}.", authx.getId(), authx.getGlobalAdminProjectId());
            globalAdminProjectId = authx.getGlobalAdminProjectId();
        } else {
            LOGGER.info("INIT: Creating Authx model {} {}.", authxDto.id(), globalAdminProjectId);
            authXService.create(authxDto, globalAdminProjectId);
        }
        LOGGER.info("INIT: Checking initial data model.");
        Optional<ProjectDto> projectDto = projectService.get(globalAdminProjectId);
        if (projectDto.isPresent()) {
            LOGGER.info("INIT: Using Global Admind Project {}.", globalAdminProjectId);
        } else {
            LOGGER.info("INIT: Creating Global Admind Project {}.", globalAdminProjectId);
            CreateProjectRequest initModel = new CreateProjectRequest(globalAdminProjectId, initialModel.description(),
                    initialModel.labels(), initialModel.adminUser(), initialModel.adminClient());
            projectService.create(initModel);
        }
    }

    @Override
    public String getGlobalAdminProjectId() {
        return globalAdminProjectId;
    }

}
