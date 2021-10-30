package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.CreateGroupRequest;
import one.microproject.authx.common.dto.GroupDto;
import one.microproject.authx.service.service.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

    @Override
    @Transactional
    public GroupDto create(String projectId, CreateGroupRequest request) {
        return null;
    }

    @Override
    public List<GroupDto> getAll() {
        return null;
    }

    @Override
    public List<GroupDto> getAll(String projectId) {
        return null;
    }

    @Override
    public Optional<GroupDto> get(String projectId, String id) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public void remove(String projectId, String id) {

    }

    @Override
    @Transactional
    public List<GroupDto> removeAll(String projectId) {
        return null;
    }

    @Override
    @Transactional
    public void removeAll() {

    }
}
