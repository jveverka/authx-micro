package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
}
