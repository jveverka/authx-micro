package one.microproject.authx.service.service.impl;

import one.microproject.authx.service.dto.CreateUserRequest;
import one.microproject.authx.service.dto.UserDto;
import one.microproject.authx.service.repository.UserRepository;
import one.microproject.authx.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static one.microproject.authx.service.service.impl.ServiceUtils.createId;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final DMapper dMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(DMapper dMapper,
                           UserRepository userRepository) {
        this.dMapper = dMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDto create(String projectId, CreateUserRequest request) {
        String dbId = createId(projectId, request.id());
        return null;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAll(String projectId) {
        return null;
    }

    @Override
    public Optional<UserDto> get(String projectId, String id) {
        String dbId = createId(projectId, id);
        return Optional.empty();
    }

    @Override
    @Transactional
    public void remove(String projectId, String id) {
        String dbId = createId(projectId, id);

    }

    @Override
    @Transactional
    public void removeAll(String projectId) {

    }

    @Override
    @Transactional
    public void setSecret(String projectId, String id, String secret) {
        String dbId = createId(projectId, id);

    }

    @Override
    public Boolean verifySecret(String projectId, String id, String secret) {
        String dbId = createId(projectId, id);
        return null;
    }

}
