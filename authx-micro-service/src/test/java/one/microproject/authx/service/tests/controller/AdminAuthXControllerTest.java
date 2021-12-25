package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.AuthxInfo;
import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ProjectReportDto;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminAuthXControllerTest extends AppBaseTest {

    @Test
    void testCreateAndDeleteProject() {
        TokenResponse globalAdminTokens = getGlobalAdminTokens();
        AuthXClient authXClient = getAuthXClient();
        BuildProjectRequest buildProjectRequest = createBuildProjectRequest("pid-001");

        AuthxInfo authxInfo = authXClient.getAuthxInfo();
        int numberOfProjects = authxInfo.projects().size();

        ResponseMessage responseMessage = authXClient.buildProject(globalAdminTokens.getAccessToken(), buildProjectRequest);
        assertTrue(responseMessage.success());
        authxInfo = authXClient.getAuthxInfo();
        assertEquals(numberOfProjects + 1, authxInfo.projects().size());

        ProjectReportDto projectReport = authXClient.getProjectReport(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertEquals(buildProjectRequest.createProjectRequest().id(), projectReport.project().id());
        assertEquals(buildProjectRequest.groups().size(), projectReport.groups().size());
        assertEquals(buildProjectRequest.roles().size(), projectReport.roles().size());
        assertEquals(buildProjectRequest.permissions().size(), projectReport.permissions().size());
        assertEquals(2, projectReport.users().size());
        assertEquals(2, projectReport.clients().size());

        responseMessage = authXClient.deleteProject(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertTrue(responseMessage.success());

        authxInfo = authXClient.getAuthxInfo();
        assertEquals(numberOfProjects, authxInfo.projects().size());
    }

}
