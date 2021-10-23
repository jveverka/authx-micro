package one.microproject.authx.service.service.impl;

import one.microproject.authx.service.dto.CreateUserRequest;
import one.microproject.authx.service.dto.UserDto;
import one.microproject.authx.service.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Override
    @Transactional
    public UserDto create(String projectId, CreateUserRequest request) {
        return null;
    }

    @Override
    public List<UserDto> getAll(String projectId) {
        return null;
    }

    @Override
    public Optional<UserDto> get(String projectId) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public void remove(String projectId, String id) {

    }

    @Override
    @Transactional
    public void setSecret(String projectId, String id, String secret) {

    }

    @Override
    public Boolean verifySecret(String projectId, String id, String secret) {
        return null;
    }

}
