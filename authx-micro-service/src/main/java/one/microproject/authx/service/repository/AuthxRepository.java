package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.Authx;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthxRepository extends MongoRepository<Authx, String> {
}
