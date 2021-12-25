package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.ClientDto;
import one.microproject.authx.common.dto.CreateClientRequest;
import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.KeyPairSerialized;
import one.microproject.authx.common.utils.CryptoUtils;
import one.microproject.authx.common.utils.LabelUtils;
import one.microproject.authx.service.exceptions.DataConflictException;
import one.microproject.authx.service.model.Client;
import one.microproject.authx.service.repository.ClientRepository;
import one.microproject.authx.service.service.ClientService;
import one.microproject.authx.service.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static one.microproject.authx.common.utils.ServiceUtils.createId;
import static one.microproject.authx.common.utils.CryptoUtils.getSha512HashBase64;
import static one.microproject.authx.common.utils.LabelUtils.AUTHX_CERTIFICATE_DURATION;


@Service
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {

    private final DMapper dMapper;
    private final CryptoService cryptoService;
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(DMapper dMapper,
                             CryptoService cryptoService,
                             ClientRepository clientRepository) {
        this.dMapper = dMapper;
        this.cryptoService = cryptoService;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public ClientDto create(String projectId, CreateClientRequest request) {
        String dbId = createId(projectId, request.id());
        Optional<Client> clientOptional = clientRepository.findById(dbId);
        if (clientOptional.isPresent()) {
            throw new DataConflictException("Client id already exists.");
        }
        String kid = UUID.randomUUID().toString();
        String secretHash = getSha512HashBase64(request.secret());
        Map<String, String> labels = LabelUtils.mergeWithDefaults(request.labels());
        Map<String, KeyPairSerialized> keys = generateKeyPair(request.id(), labels, kid);

        Client client = dMapper.map(dbId, projectId, secretHash, request, kid, keys);
        clientRepository.save(client);
        return dMapper.map(client);
    }

    @Override
    public List<ClientDto> getAll() {
        return clientRepository.findAll().stream()
                .map(dMapper::map).toList();
    }

    @Override
    public List<ClientDto> getAll(String projectId) {
        return clientRepository.findAll(projectId).stream()
                .map(dMapper::map).toList();
    }

    @Override
    public Optional<ClientDto> get(String projectId, String id) {
        String dbId = createId(projectId, id);
        Optional<Client> clientOptional = clientRepository.findById(dbId);
        return clientOptional.map(dMapper::map);
    }

    @Override
    @Transactional
    public void remove(String projectId, String id) {
        String dbId = createId(projectId, id);
        clientRepository.deleteById(dbId);
    }

    @Override
    @Transactional
    public List<ClientDto> removeAll(String projectId) {
        return clientRepository.deleteAll(projectId).stream()
                .map(dMapper::map).toList();
    }

    @Override
    public void removeAll() {
        clientRepository.deleteAll();
    }

    @Override
    @Transactional
    public void setSecret(String projectId, String id, String secret) {
        String dbId = createId(projectId, id);
        Optional<Client> clientOptional = clientRepository.findById(dbId);
        if (clientOptional.isEmpty()) {
            throw new DataConflictException("Client not found exists.");
        } else {
            String secretHash = getSha512HashBase64(secret);
            Client client = clientOptional.get();
            client.setSecret(secretHash);
            clientRepository.save(client);
        }
    }

    @Override
    public Boolean verifySecret(String projectId, String id, String secret) {
        String dbId = createId(projectId, id);
        Optional<Client> clientOptional = clientRepository.findById(dbId);
        if (clientOptional.isPresent()) {
            String secretHash = getSha512HashBase64(secret);
            return secretHash.equals(clientOptional.get().getSecret());
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Optional<KeyPairData> getDefaultKeyPair(String projectId, String id) {
        String dbId = createId(projectId, id);
        Optional<Client> clientOptional = clientRepository.findById(dbId);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            return Optional.ofNullable(CryptoUtils.map(client.getKeyPairs().get(client.getDefaultKid())));
        } else {
            return Optional.empty();
        }
    }

    private Map<String, KeyPairSerialized> generateKeyPair(String subject, Map<String, String> labels, String kid) {
        TimeUnit unit = TimeUnit.MILLISECONDS;
        Long duration = Long.parseLong(labels.get(AUTHX_CERTIFICATE_DURATION));
        KeyPairData keyPairData = cryptoService.generateKeyPair(kid, subject, unit, duration);
        KeyPairSerialized keyPairSerialized = dMapper.map(keyPairData);
        return Map.of(kid, keyPairSerialized);
    }

}
