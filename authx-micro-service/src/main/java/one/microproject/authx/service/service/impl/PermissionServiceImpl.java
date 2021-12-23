package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.PermissionDto;
import one.microproject.authx.service.exceptions.DataConflictException;
import one.microproject.authx.service.model.Permission;
import one.microproject.authx.service.repository.PermissionRepository;
import one.microproject.authx.service.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static one.microproject.authx.common.utils.ServiceUtils.createId;

@Service
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {

    private final DMapper dMapper;
    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImpl(DMapper dMapper, PermissionRepository permissionRepository) {
        this.dMapper = dMapper;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public PermissionDto create(String projectId, PermissionDto request) {
        String dbId = createId(projectId, request.id());
        Optional<Permission> optionalPermission = permissionRepository.findById(dbId);
        if (optionalPermission.isPresent()) {
            throw new DataConflictException("Permission id already exists.");
        }
        Permission permission = dMapper.map(dbId, projectId, request);
        permissionRepository.save(permission);
        return dMapper.map(permission);
    }

    @Override
    public List<PermissionDto> getAll() {
        return permissionRepository.findAll().stream().map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<PermissionDto> getAll(String projectId) {
        return permissionRepository.findAll(projectId).stream().map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public Optional<PermissionDto> get(String projectId, String id) {
        String dbId = createId(projectId, id);
        return permissionRepository.findById(dbId).map(dMapper::map);
    }

    @Override
    @Transactional
    public void remove(String projectId, String id) {
        String dbId = createId(projectId, id);
        permissionRepository.deleteById(dbId);
    }

    @Override
    @Transactional
    public void removeAll(String projectId) {
        permissionRepository.deleteAll(projectId);
    }

    @Override
    @Transactional
    public void removeAll() {
        permissionRepository.deleteAll();
    }

    @Override
    public Set<PermissionDto> getPermissions(Set<String> ids) {
        Set<PermissionDto> result = new HashSet<>();
        ids.forEach(id -> {
            Optional<Permission> permissionOptional = permissionRepository.findById(id);
            if (permissionOptional.isPresent()) {
                result.add(dMapper.map(permissionOptional.get()));
            }
        });
        return result;
    }

}
