package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.GroupDto;
import one.microproject.authx.service.exceptions.DataConflictException;
import one.microproject.authx.service.model.Group;
import one.microproject.authx.service.repository.GroupRepository;
import one.microproject.authx.service.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static one.microproject.authx.common.utils.ServiceUtils.createId;

@Service
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

    private final DMapper dMapper;
    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(DMapper dMapper, GroupRepository groupRepository) {
        this.dMapper = dMapper;
        this.groupRepository = groupRepository;
    }

    @Override
    @Transactional
    public GroupDto create(String projectId, GroupDto request) {
        String dbId = createId(projectId, request.id());
        Optional<Group> groupOptional = groupRepository.findById(dbId);
        if (groupOptional.isPresent()) {
            throw new DataConflictException("Group id already exists.");
        }
        Group group = dMapper.map(dbId, projectId, request);
        groupRepository.save(group);
        return dMapper.map(group);
    }

    @Override
    public List<GroupDto> getAll() {
        return groupRepository.findAll().stream().map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<GroupDto> getAll(String projectId) {
        return groupRepository.findAll(projectId).stream().map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public Optional<GroupDto> get(String projectId, String id) {
        String dbId = createId(projectId, id);
        return groupRepository.findById(dbId).map(dMapper::map);
    }

    @Override
    @Transactional
    public void remove(String projectId, String id) {
        String dbId = createId(projectId, id);
        groupRepository.deleteById(dbId);
    }

    @Override
    @Transactional
    public void removeAll(String projectId) {
        groupRepository.deleteAll(projectId);
    }

    @Override
    @Transactional
    public void removeAll() {
        groupRepository.deleteAll();
    }
    
}
