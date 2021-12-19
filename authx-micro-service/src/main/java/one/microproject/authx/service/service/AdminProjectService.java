package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.ProjectReportDto;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.common.dto.UpdateProjectRequest;

import java.util.List;
import java.util.Optional;

public interface AdminProjectService {

    List<ProjectDto> getAll();

    Optional<ProjectReportDto> getProjectReport(String projectId);

    ResponseMessage update(UpdateProjectRequest updateProjectRequest);

    ResponseMessage buildProject(BuildProjectRequest buildProjectRequest);

    ResponseMessage deleteRecursively(String projectId);

}
