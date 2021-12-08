package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.ClientDto;
import one.microproject.authx.common.dto.CreateClientRequest;
import one.microproject.authx.common.dto.KeyPairSerialized;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    ClientDto create(String projectId, CreateClientRequest clientRequest);

    List<ClientDto> getAll();

    List<ClientDto> getAll(String projectId);

    Optional<ClientDto> get(String projectId, String id);

    void remove(String projectId, String id);

    List<ClientDto> removeAll(String projectId);

    void removeAll();

    void setSecret(String projectId, String id, String secret);

    Boolean verifySecret(String projectId, String id, String secret);

    Optional<KeyPairSerialized> getDefaultKeyPair(String projectId, String id);

}
