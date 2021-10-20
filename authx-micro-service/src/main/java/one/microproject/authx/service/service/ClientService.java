package one.microproject.authx.service.service;

import one.microproject.authx.service.dto.ClientDto;
import one.microproject.authx.service.dto.CreateClientRequest;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    ClientDto createClient(String projectId, CreateClientRequest clientRequest);

    List<ClientDto> getAll(String projectId);

    Optional<ClientDto> get(String projectId, String id);

    void remove(String projectId, String id);

    void setSecret(String projectId, String id, String secret);

    Boolean verifySecret(String projectId, String id, String secret);

}
