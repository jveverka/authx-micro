package one.microproject.authx.service.service.impl;

import one.microproject.authx.service.dto.ClientDto;
import one.microproject.authx.service.dto.CreateProjectRequest;
import one.microproject.authx.service.dto.ProjectDto;
import one.microproject.authx.service.exceptions.DataConflictException;
import one.microproject.authx.service.model.Project;
import one.microproject.authx.service.model.User;
import one.microproject.authx.service.repository.ProjectRepository;
import one.microproject.authx.service.repository.UserRepository;
import one.microproject.authx.service.service.ClientService;
import one.microproject.authx.service.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ClientService clientService;
    private final UserRepository userRepository;

    @Autowired
    public ProjectServiceImpl(ClientService clientService,
                              ProjectRepository projectRepository,
                              UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.clientService = clientService;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public ProjectDto create(CreateProjectRequest request) {
        //data validation
        Optional<ClientDto> client = clientService.get(request.id(), request.adminClient().id());
        if (client.isPresent()) {
            throw new DataConflictException("Client already exists.");
        }
        Optional<User> user = userRepository.findById(request.adminUser().id());
        if (user.isPresent()) {
            throw new DataConflictException("User already exists.");
        }
        Optional<Project> projectOptional = projectRepository.findById(request.id());
        if (projectOptional.isPresent()) {
            throw new DataConflictException("Project already exists.");
        }
        Project project = new Project(request.id(), request.description(), List.of(request.adminUser().id()), request.labels());
        projectRepository.save(project);
        clientService.createClient(request.id(), request.adminClient());
        return new ProjectDto(request.id(), request.description(), request.labels());
    }

    @Override
    public List<ProjectDto> getAll() {
        return projectRepository.findAll().stream().map(p -> new ProjectDto(p.getId(), p.getDescription(), p.getLabels())).collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectDto> get(String id) {
        return projectRepository.findById(id).map(p -> new ProjectDto(p.getId(), p.getDescription(), p.getLabels()));
    }

    @Override
    @Transactional
    public void remove(String id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isEmpty()) {
            throw new DataConflictException("Project not found.");
        } else {
            //TODO: delete all project dependencies
            projectRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public void removeAll() {
        projectRepository.deleteAll();
    }

}
