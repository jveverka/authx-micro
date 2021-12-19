package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.ResponseMessage;

import java.util.List;

public interface AdminAuthXService {

    List<ProjectDto> getAll();

    ResponseMessage buildProject(BuildProjectRequest buildProjectRequest);

    ResponseMessage deleteRecursively(String projectId);

}
