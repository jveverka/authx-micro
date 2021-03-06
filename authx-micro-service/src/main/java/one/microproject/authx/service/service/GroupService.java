package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.GroupDto;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    GroupDto create(String projectId, GroupDto request);

    List<GroupDto> getAll();

    List<GroupDto> getAll(String projectId);

    Optional<GroupDto> get(String projectId, String id);

    void remove(String projectId, String id);

    void removeAll(String projectId);

    void removeAll();

}
