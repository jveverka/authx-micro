package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.CreateProjectRequest;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.UpdateProjectRequest;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    ProjectDto create(CreateProjectRequest request);

    List<ProjectDto> getAll();

    Optional<ProjectDto> get(String id);

    void remove(String id);

    void removeAll();

    void update(UpdateProjectRequest request);

}
