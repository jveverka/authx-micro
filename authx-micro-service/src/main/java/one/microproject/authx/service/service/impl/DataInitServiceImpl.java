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
import java.util.List;
import java.util.Optional;

@Service
public class DataInitServiceImpl implements DataInitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitServiceImpl.class);

    private final AuthxDto authxDto;
    private final CreateProjectRequest initialModel;
    private final ProjectService projectService;
    private final AuthXService authXService;

    private List<String> globalAdminProjectIds;

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
        globalAdminProjectIds = List.of(initialModel.id());
        LOGGER.info("INIT: Checking initial Authx model.");
        Optional<Authx> authxInfo = authXService.getAuthxInfo();
        boolean createGlobalAdminProject = false;
        if (authxInfo.isPresent()) {
            Authx authx = authxInfo.get();
            LOGGER.info("INIT: Using Authx model {} {}.", authx.getId(), authx.getGlobalAdminProjectIds());
            globalAdminProjectIds = authx.getGlobalAdminProjectIds();
        } else {
            LOGGER.info("INIT: Creating Authx model {} {}.", authxDto.id(), globalAdminProjectIds);
            authXService.createOrUpdate(authxDto, globalAdminProjectIds);
            createGlobalAdminProject = true;
        }
        if (createGlobalAdminProject) {
            LOGGER.info("INIT: Checking initial data model.");
            String globalAdminProjectId = initialModel.id();
            Optional<ProjectDto> projectDto = projectService.get(globalAdminProjectId);
            if (projectDto.isPresent()) {
                LOGGER.info("INIT: Using Global Admin Project {}.", globalAdminProjectId);
            } else {
                LOGGER.info("INIT: Creating Global Admin Project {}.", globalAdminProjectId);
                CreateProjectRequest initModel = new CreateProjectRequest(globalAdminProjectId, initialModel.description(),
                        initialModel.labels(), initialModel.adminUser(), initialModel.adminClient());
                projectService.create(initModel);
            }
        } else {
            LOGGER.info("INIT: Global Admin Project create skipped !");
        }
    }

    @Override
    public List<String> getGlobalAdminProjectIds() {
        return globalAdminProjectIds;
    }

}
