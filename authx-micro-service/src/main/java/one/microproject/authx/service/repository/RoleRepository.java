package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
}
