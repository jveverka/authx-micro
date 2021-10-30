package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.CreateGroupRequest;
import one.microproject.authx.common.dto.GroupDto;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    GroupDto create(String projectId, CreateGroupRequest request);

    List<GroupDto> getAll();

    List<GroupDto> getAll(String projectId);

    Optional<GroupDto> get(String projectId, String id);

    void remove(String projectId, String id);

    List<GroupDto> removeAll(String projectId);

    void removeAll();

}
