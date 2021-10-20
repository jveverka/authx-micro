package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PermissionRepository extends MongoRepository<Permission, String> {
}
