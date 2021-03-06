package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.ClientDto;
import one.microproject.authx.common.dto.CreateProjectRequest;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.UpdateProjectRequest;
import one.microproject.authx.common.dto.UserDto;
import one.microproject.authx.service.exceptions.DataConflictException;
import one.microproject.authx.service.model.Project;
import one.microproject.authx.service.repository.ProjectRepository;
import one.microproject.authx.service.service.ClientService;
import one.microproject.authx.service.service.ProjectService;
import one.microproject.authx.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final DMapper dMapper;
    private final ProjectRepository projectRepository;
    private final ClientService clientService;
    private final UserService userService;

    @Autowired
    public ProjectServiceImpl(DMapper dMapper,
                              ProjectRepository projectRepository,
                              ClientService clientService,
                              UserService userService) {
        this.dMapper = dMapper;
        this.projectRepository = projectRepository;
        this.clientService = clientService;
        this.userService = userService;
    }


    @Override
    @Transactional
    public ProjectDto create(CreateProjectRequest request) {
        //data validation
        Optional<ClientDto> client = clientService.get(request.id(), request.adminClient().id());
        if (client.isPresent()) {
            throw new DataConflictException("Client already exists.");
        }
        Optional<UserDto> user = userService.get(request.id(), request.adminUser().id());
        if (user.isPresent()) {
            throw new DataConflictException("User already exists.");
        }
        Optional<Project> projectOptional = projectRepository.findById(request.id());
        if (projectOptional.isPresent()) {
            throw new DataConflictException("Project already exists.");
        }
        Project project = dMapper.map(request);
        projectRepository.save(project);
        clientService.create(request.id(), request.adminClient());
        userService.create(request.id(), request.adminUser());
        return dMapper.map(project);
    }

    @Override
    public List<ProjectDto> getAll() {
        return projectRepository.findAll().stream().map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectDto> get(String id) {
        return projectRepository.findById(id).map(dMapper::map);
    }

    @Override
    public boolean isAdmin(String id, String userId) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            return projectOptional.get().getAdminIds().contains(userId);
        }
        return false;
    }

    @Override
    @Transactional
    public void remove(String id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isEmpty()) {
            throw new DataConflictException("Project not found.");
        } else {
            clientService.removeAll(id);
            userService.removeAll(id);
            projectRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public void removeAll() {
        projectRepository.deleteAll();
    }

    @Override
    public void update(UpdateProjectRequest request) {
        Optional<Project> projectOptional = projectRepository.findById(request.id());
        if (projectOptional.isEmpty()) {
            throw new DataConflictException("Project not found.");
        }
        Project project = projectOptional.get();
        project.setDescription(request.description());
        project.setLabels(request.labels());
        projectRepository.save(project);
    }

}
