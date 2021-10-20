package one.microproject.authx.service.service.impl;

import one.microproject.authx.service.dto.CreateProjectRequest;
import one.microproject.authx.service.dto.ProjectDto;
import one.microproject.authx.service.model.Project;
import one.microproject.authx.service.repository.ProjectRepository;
import one.microproject.authx.service.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    @Override
    public ProjectDto createProject(CreateProjectRequest request) {
        Project project = new Project(request.id(), request.description(), request.labels());
        projectRepository.save(project);
        return new ProjectDto(request.id(), request.description(), request.labels());
    }

    @Override
    public List<ProjectDto> getProjects() {
        return projectRepository.findAll().stream().map(p -> new ProjectDto(p.getId(), p.getDescription(), p.getLabels())).collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectDto> getProject(String id) {
        return projectRepository.findById(id).map(p -> new ProjectDto(p.getId(), p.getDescription(), p.getLabels()));
    }

    @Override
    public void removeProject(String id) {
        projectRepository.deleteById(id);
    }

    @Override
    public void removeAll() {
        projectRepository.deleteAll();
    }

}
