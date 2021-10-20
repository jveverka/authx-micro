package one.microproject.authx.service.service;

import one.microproject.authx.service.dto.CreateProjectRequest;
import one.microproject.authx.service.dto.ProjectDto;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    ProjectDto createProject(CreateProjectRequest request);

    List<ProjectDto> getProjects();

    Optional<ProjectDto> getProject(String id);

    void removeProject(String id);

    void removeAll();

}
