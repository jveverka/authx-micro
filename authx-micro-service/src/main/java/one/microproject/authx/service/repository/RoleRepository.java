package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RoleRepository extends MongoRepository<Role, String> {

    @Query(value="{projectId:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
    List<Role> findAll(String projectId);

    @Query(value="{projectId:'?0'}", fields="{'name' : 1, 'quantity' : 1}", delete = true)
    List<Role> deleteAll(String projectId);

}
