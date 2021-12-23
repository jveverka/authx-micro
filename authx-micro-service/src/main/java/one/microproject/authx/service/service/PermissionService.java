package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.PermissionDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionService {

    PermissionDto create(String projectId, PermissionDto request);

    List<PermissionDto> getAll();

    List<PermissionDto> getAll(String projectId);

    Optional<PermissionDto> get(String projectId, String id);

    void remove(String projectId, String id);

    void removeAll(String projectId);

    void removeAll();

    Set<PermissionDto> getPermissions(Set<String> ids);

}
