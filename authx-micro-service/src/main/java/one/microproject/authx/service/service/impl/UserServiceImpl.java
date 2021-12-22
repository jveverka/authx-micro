package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.CreateUserRequest;
import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.KeyPairSerialized;
import one.microproject.authx.common.dto.UserDto;
import one.microproject.authx.common.utils.CryptoUtils;
import one.microproject.authx.common.utils.LabelUtils;
import one.microproject.authx.service.exceptions.DataConflictException;
import one.microproject.authx.service.model.User;
import one.microproject.authx.service.repository.UserRepository;
import one.microproject.authx.service.service.CryptoService;
import one.microproject.authx.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static one.microproject.authx.common.utils.ServiceUtils.createId;
import static one.microproject.authx.common.utils.CryptoUtils.getSha512HashBase64;
import static one.microproject.authx.common.utils.LabelUtils.AUTHX_CERTIFICATE_DURATION;
import static one.microproject.authx.common.utils.LabelUtils.PRIMARY_KID;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final DMapper dMapper;
    private final CryptoService cryptoService;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(DMapper dMapper,
                           CryptoService cryptoService,
                           UserRepository userRepository) {
        this.dMapper = dMapper;
        this.cryptoService = cryptoService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDto create(String projectId, CreateUserRequest request) {
        String dbId = createId(projectId, request.id());
        Optional<User> userOptional = userRepository.findById(dbId);
        if (userOptional.isPresent()) {
            throw new DataConflictException("User id already exists.");
        }
        String secretHash = getSha512HashBase64(request.secret());
        Map<String, String> labels = LabelUtils.mergeWithDefaults(request.labels());
        Map<String, KeyPairSerialized> keys = generateKeyPair(request.id(), labels);

        User user = dMapper.map(dbId, projectId, request.clientId(), secretHash, request, PRIMARY_KID, keys);
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

    @Override
    public Optional<KeyPairData> getDefaultKeyPair(String projectId, String id) {
        String dbId = createId(projectId, id);
        Optional<User> userOptional = userRepository.findById(dbId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return Optional.ofNullable(CryptoUtils.map(user.getKeyPairs().get(user.getDefaultKid())));
        } else {
            return Optional.empty();
        }
    }

    private Map<String, KeyPairSerialized> generateKeyPair(String subject, Map<String, String> labels) {
        TimeUnit unit = TimeUnit.MILLISECONDS;
        Long duration = Long.parseLong(labels.get(AUTHX_CERTIFICATE_DURATION));
        KeyPairData keyPairData = cryptoService.generateKeyPair(PRIMARY_KID, subject, unit, duration);
        KeyPairSerialized keyPairSerialized = dMapper.map(keyPairData);
        return Map.of(PRIMARY_KID, keyPairSerialized);
    }

}
