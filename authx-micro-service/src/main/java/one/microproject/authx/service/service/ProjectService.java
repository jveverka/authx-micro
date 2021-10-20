package one.microproject.authx.service.service;

import one.microproject.authx.service.dto.CreateProjectRequest;
import one.microproject.authx.service.dto.ProjectDto;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    ProjectDto create(CreateProjectRequest request);

    List<ProjectDto> getAll();

    Optional<ProjectDto> get(String id);

    void remove(String id);

    void removeAll();

}
