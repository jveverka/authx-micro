package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PermissionRepository extends MongoRepository<Permission, String> {

    @Query(value="{ 'projectId' : ?0 }")
    List<Permission> findAll(String projectId);

    @Query(value="{ 'projectId' : ?0 }", delete = true)
    List<Permission> deleteAll(String projectId);

}
