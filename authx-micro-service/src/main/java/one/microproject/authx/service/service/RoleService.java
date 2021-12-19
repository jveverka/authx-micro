package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.RoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    RoleDto create(String projectId, RoleDto request);

    List<RoleDto> getAll();

    List<RoleDto> getAll(String projectId);

    Optional<RoleDto> get(String projectId, String id);

    void remove(String projectId, String id);

    void removeAll(String projectId);

    void removeAll();

}
