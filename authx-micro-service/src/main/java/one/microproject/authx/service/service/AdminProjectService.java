package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.ProjectReportDto;
import one.microproject.authx.common.dto.UpdateProjectRequest;

import java.util.Optional;

public interface AdminProjectService {

    Optional<ProjectReportDto> getProjectReport(String projectId);

    void update(UpdateProjectRequest updateProjectRequest);

}
