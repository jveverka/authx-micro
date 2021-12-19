package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.PermissionDto;
import one.microproject.authx.service.exceptions.DataConflictException;
import one.microproject.authx.service.model.Permission;
import one.microproject.authx.service.repository.PermissionRepository;
import one.microproject.authx.service.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return null;
    }

    @Override
    public List<PermissionDto> getAll() {
        return null;
    }

    @Override
    public List<PermissionDto> getAll(String projectId) {
        return null;
    }

    @Override
    public Optional<PermissionDto> get(String projectId, String id) {
        String dbId = createId(projectId, id);
        return Optional.empty();
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

}
