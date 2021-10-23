package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    @Query(value="{projectId:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
    List<User> findAll(String projectId);

    @Query(value="{clientId:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
    Optional<User> findByClientId(String clientId);

    @Query(value="{projectId:'?0'}", fields="{'name' : 1, 'quantity' : 1}", delete = true)
    List<User> deleteAll(String projectId);

}
