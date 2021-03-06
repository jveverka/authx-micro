package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.RoleDto;
import one.microproject.authx.service.exceptions.DataConflictException;
import one.microproject.authx.service.model.Role;
import one.microproject.authx.service.repository.RoleRepository;
import one.microproject.authx.service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static one.microproject.authx.common.utils.ServiceUtils.createId;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final DMapper dMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(DMapper dMapper, RoleRepository roleRepository) {
        this.dMapper = dMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public RoleDto create(String projectId, RoleDto request) {
        String dbId = createId(projectId, request.id());
        Optional<Role> roleOptional = roleRepository.findById(dbId);
        if (roleOptional.isPresent()) {
            throw new DataConflictException("Role id already exists.");
        }
        Role role = dMapper.map(dbId, projectId, request);
        roleRepository.save(role);
        return dMapper.map(role);
    }

    @Override
    public List<RoleDto> getAll() {
        return roleRepository.findAll().stream().map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<RoleDto> getAll(String projectId) {
        return roleRepository.findAll(projectId).stream().map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDto> get(String projectId, String id) {
        String dbId = createId(projectId, id);
        return roleRepository.findById(dbId).map(dMapper::map);
    }

    @Override
    @Transactional
    public void remove(String projectId, String id) {
        String dbId = createId(projectId, id);
        roleRepository.deleteById(dbId);
    }

    @Override
    @Transactional
    public void removeAll(String projectId) {
        roleRepository.deleteAll(projectId);
    }

    @Override
    @Transactional
    public void removeAll() {
        roleRepository.deleteAll();
    }

}
