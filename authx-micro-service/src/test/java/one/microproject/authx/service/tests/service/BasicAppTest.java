package one.microproject.authx.service.tests.service;

import one.microproject.authx.service.dto.CreateProjectRequest;
import one.microproject.authx.service.dto.ProjectDto;
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

class BasicAppTest extends AppBaseTest {

    @Autowired
    ProjectService projectService;

    @Test
    void contextLoads() {
        assertNotNull(projectService);
    }

    @Test
    void testProjectAddRemove() {
        CreateProjectRequest createProjectRequest = new CreateProjectRequest("p-001", "P 001", Map.of("key", "value"));
        ProjectDto project = projectService.createProject(createProjectRequest);
        assertNotNull(project);
        assertEquals(createProjectRequest.id(), project.id());

        List<ProjectDto> projects = projectService.getProjects();
        assertEquals(1, projects.size());

        Optional<ProjectDto> projectDtoOptional = projectService.getProject(project.id());
        assertTrue(projectDtoOptional.isPresent());
        assertEquals(createProjectRequest.id(), projectDtoOptional.get().id());

        projectService.removeProject(project.id());
        projects = projectService.getProjects();
        assertEquals(0, projects.size());
    }

    @AfterEach
    private void cleanup() {
        projectService.removeAll();
    }

}
