package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.CreateProjectRequest;
import one.microproject.authx.common.dto.ProjectDto;
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

    private final CreateProjectRequest initialModel;
    private final ProjectService projectService;

    @Autowired
    public DataInitServiceImpl(CreateProjectRequest initialModel, ProjectService projectService) {
        this.initialModel = initialModel;
        this.projectService = projectService;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("INIT: Creating initial data model");
        Optional<ProjectDto> projectDto = projectService.get(initialModel.id());
        if (projectDto.isPresent()) {
            LOGGER.info("INIT: Project {} already exists !", projectDto.get().id());
            return;
        } else {
            projectService.create(initialModel);
        }
    }

    @Override
    public String getGlobalAdminProjectId() {
        return initialModel.id();
    }

}
