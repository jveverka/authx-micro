package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.CreateUserRequest;
import one.microproject.authx.common.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto create(String projectId, String clientId, CreateUserRequest request);

    List<UserDto> getAll();

    List<UserDto> getAll(String projectId);

    Optional<UserDto> get(String projectId, String id);

    void remove(String projectId, String id);

    void removeAll(String projectId);

    void removeAll();

    void setSecret(String projectId, String id, String secret);

    Boolean verifySecret(String projectId, String id, String secret);

}
