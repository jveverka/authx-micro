package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.CreateClientRequest;
import one.microproject.authx.common.dto.CreateProjectRequest;
import one.microproject.authx.common.dto.CreateUserRequest;
import one.microproject.authx.common.dto.GroupDto;
import one.microproject.authx.common.dto.PermissionDto;
import one.microproject.authx.common.dto.ProjectReportDto;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminAuthXControllerTest extends AppBaseTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void testCreateAndDeleteProject() throws IOException {
        TokenResponse globalAdminTokens = getGlobalAdminTokens();
        AuthXClient authXClient = getAuthXClient();
        BuildProjectRequest buildProjectRequest = createBuildProjectRequest();
        ResponseMessage responseMessage = authXClient.buildProject(globalAdminTokens.getAccessToken(), buildProjectRequest);
        assertTrue(responseMessage.success());
        ProjectReportDto projectReport = authXClient.getProjectReport(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertEquals(buildProjectRequest.createProjectRequest().id(), projectReport.project().id());
        responseMessage = authXClient.deleteProject(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertTrue(responseMessage.success());
    }

    private BuildProjectRequest createBuildProjectRequest() throws JsonProcessingException {
        CreateUserRequest adminUser = new CreateUserRequest("admin-user-001", "admin@pid-001", "", "", Map.of(), Set.of(), Set.of(), "id");
        CreateClientRequest adminClient = new CreateClientRequest("admin-client-001", "", false, "", Map.of(), Set.of(), Set.of());
        CreateProjectRequest cp = new CreateProjectRequest("pid-001", "desc", Map.of(), adminUser, adminClient);
        List<GroupDto> groups = List.of(new GroupDto("g-001", "pid-001", "description", Map.of()));
        List<PermissionDto> permissions = List.of(new PermissionDto("p-001", "pid-001", "description", "service", "resource", "action"));
        return new BuildProjectRequest(cp, groups, List.of(), permissions, List.of(), List.of());
    }


}
