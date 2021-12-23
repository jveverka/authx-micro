package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.AuthxInfo;
import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.CreateClientRequest;
import one.microproject.authx.common.dto.CreateProjectRequest;
import one.microproject.authx.common.dto.CreateUserRequest;
import one.microproject.authx.common.dto.GroupDto;
import one.microproject.authx.common.dto.PermissionDto;
import one.microproject.authx.common.dto.ProjectReportDto;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.common.dto.RoleDto;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminAuthXControllerTest extends AppBaseTest {

    @Test
    void testCreateAndDeleteProject() throws IOException {
        TokenResponse globalAdminTokens = getGlobalAdminTokens();
        AuthXClient authXClient = getAuthXClient();
        BuildProjectRequest buildProjectRequest = createBuildProjectRequest();

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

    private BuildProjectRequest createBuildProjectRequest() throws JsonProcessingException {
        CreateUserRequest adminUser = new CreateUserRequest("admin-user-001", "admin@pid-001", "", "", Map.of(), Set.of(), Set.of(), "id");
        CreateClientRequest adminClient = new CreateClientRequest("admin-client-001", "", false, "", Map.of(), Set.of(), Set.of());
        CreateProjectRequest cp = new CreateProjectRequest("pid-001", "desc", Map.of(), adminUser, adminClient);
        List<GroupDto> groups = List.of(new GroupDto("g-001", "description", Map.of()));
        List<PermissionDto> permissions = List.of(new PermissionDto("p-001", "description", "service", "resource", "action"));
        List<RoleDto> roles = List.of(new RoleDto("r-001", "description", Set.of("p-001")));
        CreateClientRequest normalClient = new CreateClientRequest("cl-001", "", true, "secret", Map.of(), Set.of("r-001"), Set.of("g-001"));
        CreateUserRequest normalUser = new CreateUserRequest("u-001", "", "", "", Map.of(), Set.of("r-001"), Set.of("g-001"), "cl-001");
        return new BuildProjectRequest(cp, groups, roles, permissions, List.of(normalClient), List.of(normalUser));
    }


}
