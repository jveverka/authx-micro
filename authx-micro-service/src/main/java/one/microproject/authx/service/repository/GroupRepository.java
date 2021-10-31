package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {

    @Query(value="{projectId:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
    List<Group> findAll(String projectId);

    @Query(value="{projectId:'?0'}", fields="{'name' : 1, 'quantity' : 1}", delete = true)
    List<Group> deleteAll(String projectId);

}
