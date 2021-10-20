package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
}
