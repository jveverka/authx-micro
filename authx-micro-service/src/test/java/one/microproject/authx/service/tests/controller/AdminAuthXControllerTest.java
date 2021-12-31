package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.*;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminAuthXControllerTest extends AppBaseTest {

    @Test
    void testCreateAndDeleteProject() {
        TokenResponse globalAdminTokens = getGlobalAdminTokens();
        AuthXClient authXClient = getAuthXClient();
        BuildProjectRequest buildProjectRequest = createBuildProjectRequest("pid-001");

        AuthXResponse<AuthXInfo, Void> authXResponse = authXClient.getAuthXInfo();
        int numberOfProjects = authXResponse.response().projects().size();

        AuthXResponse<String, ErrorMessage> responseMessage = authXClient.buildProject(globalAdminTokens.getAccessToken(), buildProjectRequest);
        assertTrue(responseMessage.isSuccess());
        authXResponse = authXClient.getAuthXInfo();
        assertEquals(numberOfProjects + 1, authXResponse.response().projects().size());

        AuthXResponse<ProjectReportDto, Void> projectReportResponse = authXClient.getProjectReport(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertTrue(projectReportResponse.isSuccess());
        ProjectReportDto projectReport = projectReportResponse.response();
        assertEquals(buildProjectRequest.createProjectRequest().id(), projectReport.project().id());
        assertEquals(buildProjectRequest.groups().size(), projectReport.groups().size());
        assertEquals(buildProjectRequest.roles().size(), projectReport.roles().size());
        assertEquals(buildProjectRequest.permissions().size(), projectReport.permissions().size());
        assertEquals(2, projectReport.users().size());
        assertEquals(2, projectReport.clients().size());

        responseMessage = authXClient.deleteProject(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertTrue(responseMessage.isSuccess());

        authXResponse = authXClient.getAuthXInfo();
        assertEquals(numberOfProjects, authXResponse.response().projects().size());
    }

    @Test
    void testUpdateProject() {
        TokenResponse globalAdminTokens = getGlobalAdminTokens();
        AuthXClient authXClient = getAuthXClient();
        BuildProjectRequest buildProjectRequest = createBuildProjectRequest("pid-004");

        AuthXResponse<String, ErrorMessage> responseMessage = authXClient.buildProject(globalAdminTokens.getAccessToken(), buildProjectRequest);
        assertTrue(responseMessage.isSuccess());

        UpdateProjectRequest updateProjectRequest = new UpdateProjectRequest("pid-004", "new description", Map.of());
        responseMessage = authXClient.update(globalAdminTokens.getAccessToken(), updateProjectRequest);
        assertTrue(responseMessage.isSuccess());

        AuthXResponse<ProjectReportDto, Void> projectReport = authXClient.getProjectReport(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertEquals("new description", projectReport.response().project().description());

        responseMessage = authXClient.deleteProject(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertTrue(responseMessage.isSuccess());
    }

}
