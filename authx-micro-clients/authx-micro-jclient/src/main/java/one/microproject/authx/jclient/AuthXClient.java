package one.microproject.authx.jclient;

import one.microproject.authx.common.dto.*;

public interface AuthXClient {

    AuthXResponse<AuthXInfo, Void> getAuthXInfo();

    AuthXOAuth2Client getAuthXOAuth2Client(String projectId);

    AuthXResponse<String, ErrorMessage> buildProject(String token, BuildProjectRequest buildProjectRequest);

    AuthXResponse<String, ErrorMessage> deleteProject(String token, String projectId);

    AuthXResponse<ProjectReportDto, Void> getProjectReport(String token, String projectId);

    AuthXResponse<String, ErrorMessage> update(String token, UpdateProjectRequest updateProjectRequest);

}
