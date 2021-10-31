package one.microproject.authx.service.tests.service;

import one.microproject.authx.common.dto.CreateClientRequest;
import one.microproject.authx.common.dto.CreateProjectRequest;
import one.microproject.authx.common.dto.CreateUserRequest;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.service.service.ProjectService;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static com.mongodb.assertions.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectServiceTest extends AppBaseTest {

    @Autowired
    ProjectService projectService;

    @Test
    void testProjectAddRemove() {
        CreateUserRequest adminUser = new CreateUserRequest("admin", "user@domain.com", "d", "secret", Map.of());
        CreateClientRequest adminClient = new CreateClientRequest("admin-client", "description", false, "secret", Map.of());
        CreateProjectRequest createProjectRequest = new CreateProjectRequest("p-001", "P 001", Map.of("key", "value"), adminUser, adminClient);
        ProjectDto project = projectService.create(createProjectRequest);
        assertNotNull(project);
        assertEquals(createProjectRequest.id(), project.id());

        List<ProjectDto> projects = projectService.getAll();
        assertEquals(2, projects.size());

        Optional<ProjectDto> projectDtoOptional = projectService.get(project.id());
        assertTrue(projectDtoOptional.isPresent());
        assertEquals(createProjectRequest.id(), projectDtoOptional.get().id());

        projectService.remove(project.id());
        projects = projectService.getAll();
        assertEquals(1, projects.size());
    }

    @AfterEach
    private void cleanup() {
        projectService.removeAll();
    }

}
