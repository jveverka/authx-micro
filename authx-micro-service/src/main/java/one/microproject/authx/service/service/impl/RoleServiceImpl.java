package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.RoleDto;
import one.microproject.authx.service.repository.RoleRepository;
import one.microproject.authx.service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return null;
    }

    @Override
    public List<RoleDto> getAll() {
        return null;
    }

    @Override
    public List<RoleDto> getAll(String projectId) {
        return null;
    }

    @Override
    public Optional<RoleDto> get(String projectId, String id) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public void remove(String projectId, String id) {

    }

    @Override
    @Transactional
    public void removeAll(String projectId) {

    }

    @Override
    @Transactional
    public void removeAll() {

    }
    
}
