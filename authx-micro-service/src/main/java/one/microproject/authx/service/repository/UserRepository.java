package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
