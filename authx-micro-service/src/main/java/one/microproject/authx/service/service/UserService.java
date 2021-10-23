package one.microproject.authx.service.service;

import one.microproject.authx.service.dto.CreateUserRequest;
import one.microproject.authx.service.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto create(String projectId, CreateUserRequest request);

    List<UserDto> getAll();

    List<UserDto> getAll(String projectId);

    Optional<UserDto> get(String projectId, String id);

    void remove(String projectId, String id);

    void removeAll(String projectId);

    void setSecret(String projectId, String id, String secret);

    Boolean verifySecret(String projectId, String id, String secret);

}
