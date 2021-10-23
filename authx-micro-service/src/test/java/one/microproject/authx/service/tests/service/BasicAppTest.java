package one.microproject.authx.service.tests.service;

import one.microproject.authx.service.dto.ClientDto;
import one.microproject.authx.service.dto.CreateClientRequest;
import one.microproject.authx.service.dto.CreateProjectRequest;
import one.microproject.authx.service.dto.CreateUserRequest;
import one.microproject.authx.service.dto.ProjectDto;
import one.microproject.authx.service.service.ClientService;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

class BasicAppTest extends AppBaseTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    ClientService clientService;

    @Test
    void contextLoads() {
        assertNotNull(projectService);
    }

    @Test
    void testProjectAddRemove() {
        CreateUserRequest adminUser = new CreateUserRequest("admin");
        CreateClientRequest adminClient = new CreateClientRequest("admin-client", "description", false, "secret", Map.of());
        CreateProjectRequest createProjectRequest = new CreateProjectRequest("p-001", "P 001", Map.of("key", "value"), adminUser, adminClient);
        ProjectDto project = projectService.create(createProjectRequest);
        assertNotNull(project);
        assertEquals(createProjectRequest.id(), project.id());

        List<ProjectDto> projects = projectService.getAll();
        assertEquals(1, projects.size());

        Optional<ProjectDto> projectDtoOptional = projectService.get(project.id());
        assertTrue(projectDtoOptional.isPresent());
        assertEquals(createProjectRequest.id(), projectDtoOptional.get().id());

        projectService.remove(project.id());
        projects = projectService.getAll();
        assertEquals(0, projects.size());
    }

    @Test
    void testClientAddAndRemove() {
        CreateClientRequest createClientRequest1 = new CreateClientRequest("c1", "d11", true, "s11", Map.of());
        CreateClientRequest createClientRequest2 = new CreateClientRequest("c1", "d21", true, "s21", Map.of());
        ClientDto client1 = clientService.createClient("p-01", createClientRequest1);
        ClientDto client2 = clientService.createClient("p-02", createClientRequest2);

        assertEquals(createClientRequest1.id(), client1.id());
        assertEquals(createClientRequest2.id(), client2.id());

        List<ClientDto> clients = clientService.getAll();
        assertEquals(2, clients.size());

        clients = clientService.getAll("p-01");
        assertEquals(1, clients.size());

        clients = clientService.getAll("p-02");
        assertEquals(1, clients.size());

        clients = clientService.getAll("p-xx");
        assertEquals(0, clients.size());

        assertTrue(clientService.verifySecret("p-01", "c1", "s11"));
        assertFalse(clientService.verifySecret("p-02", "c1", "s11"));

        clients = clientService.removeAll("p-01");
        assertEquals(1, clients.size());

        clients = clientService.getAll();
        assertEquals(1, clients.size());

        clients = clientService.removeAll("p-02");
        assertEquals(1, clients.size());

        clients = clientService.getAll();
        assertEquals(0, clients.size());
    }

    @AfterEach
    private void cleanup() {
        projectService.removeAll();
    }

}
