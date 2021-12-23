package one.microproject.authx.jclient;

import one.microproject.authx.common.dto.AuthxInfo;
import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ProjectReportDto;
import one.microproject.authx.common.dto.ResponseMessage;

public interface AuthXClient {

    AuthxInfo getAuthxInfo();

    AuthXOAuth2Client getAuthXOAuth2Client(String projectId);

    ResponseMessage buildProject(String token, BuildProjectRequest buildProjectRequest);

    ResponseMessage deleteProject(String token, String projectId);

    ProjectReportDto getProjectReport(String token, String projectId);

}
