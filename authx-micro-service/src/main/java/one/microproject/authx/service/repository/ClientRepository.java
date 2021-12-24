package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ClientRepository extends MongoRepository<Client, String> {

    @Query(value="{ 'projectId' : ?0 }")
    List<Client> findAll(String projectId);

    @Query(value="{ 'projectId' : ?0 }", delete = true)
    List<Client> deleteAll(String projectId);

}
