package one.microproject.authx.service.service.impl;

import one.microproject.authx.service.dto.ClientDto;
import one.microproject.authx.service.dto.CreateClientRequest;
import one.microproject.authx.service.exceptions.DataConflictException;
import one.microproject.authx.service.model.Client;
import one.microproject.authx.service.repository.ClientRepository;
import one.microproject.authx.service.service.ClientService;
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
public class ClientServiceImpl implements ClientService {

    private final DMapper dMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(DMapper dMapper, ClientRepository clientRepository) {
        this.dMapper = dMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public ClientDto createClient(String projectId, CreateClientRequest clientRequest) {
        String dbId = createId(projectId, clientRequest.id());
        Optional<Client> clientOptional = clientRepository.findById(dbId);
        if (clientOptional.isPresent()) {
            throw new DataConflictException("Client id already exists.");
        }
        String secretHash = getSha512HashBase64(clientRequest.secret());
        Client client = dMapper.map(dbId, projectId, secretHash, clientRequest);
        clientRepository.save(client);
        return dMapper.map(client);
    }

    @Override
    public List<ClientDto> getAll() {
        return clientRepository.findAll().stream()
                .map(dMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<ClientDto> getAll(String projectId) {
        return clientRepository.findAll(projectId).stream()
                .map(dMapper::map).collect(Collectors.toList());
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
                .map(dMapper::map).collect(Collectors.toList());
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

}
