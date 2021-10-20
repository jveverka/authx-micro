package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
}
