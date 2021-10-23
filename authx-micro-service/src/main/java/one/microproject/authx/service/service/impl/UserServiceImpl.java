package one.microproject.authx.service.service.impl;

import one.microproject.authx.service.dto.CreateUserRequest;
import one.microproject.authx.service.dto.UserDto;
import one.microproject.authx.service.exceptions.DataConflictException;
import one.microproject.authx.service.model.User;
import one.microproject.authx.service.repository.UserRepository;
import one.microproject.authx.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static one.microproject.authx.service.service.impl.ServiceUtils.createId;
import static one.microproject.authx.service.service.impl.ServiceUtils.getSha512HashBase64;

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
    public UserDto create(String projectId, String clientId, CreateUserRequest request) {
        String dbId = createId(projectId, request.id());
        Optional<User> userOptional = userRepository.findById(dbId);
        if (userOptional.isPresent()) {
            throw new DataConflictException("User id already exists.");
        }
        String secretHash = getSha512HashBase64(request.secret());
        User user = dMapper.map(dbId, projectId, clientId, secretHash, request);
        userRepository.save(user);
        return dMapper.map(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAll(String projectId) {
        return userRepository.findAll(projectId).stream().map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> get(String projectId, String id) {
        String dbId = createId(projectId, id);
        return userRepository.findById(dbId).map(dMapper::map);
    }

    @Override
    @Transactional
    public void remove(String projectId, String id) {
        String dbId = createId(projectId, id);
        userRepository.deleteById(dbId);
    }

    @Override
    @Transactional
    public void removeAll(String projectId) {
        userRepository.deleteAll(projectId);
    }

    @Override
    public void removeAll() {
        userRepository.deleteAll();
    }

    @Override
    @Transactional
    public void setSecret(String projectId, String id, String secret) {
        String dbId = createId(projectId, id);
        Optional<User> userOptional = userRepository.findById(dbId);
        if (userOptional.isEmpty()) {
            throw new DataConflictException("User not found.");
        } else {
            User user = userOptional.get();
            String secretHash = getSha512HashBase64(secret);
            user.setSecret(secretHash);
            userRepository.save(user);
        }
    }

    @Override
    public Boolean verifySecret(String projectId, String id, String secret) {
        String dbId = createId(projectId, id);
        Optional<User> userOptional = userRepository.findById(dbId);
        if (userOptional.isPresent()) {
            String secretHash = getSha512HashBase64(secret);
            return secretHash.equals(userOptional.get().getSecret());
        } else {
            return Boolean.FALSE;
        }
    }

}
