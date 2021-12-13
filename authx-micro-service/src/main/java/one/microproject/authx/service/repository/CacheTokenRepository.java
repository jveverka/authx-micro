package one.microproject.authx.service.repository;

import one.microproject.authx.service.model.CachedToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheTokenRepository extends CrudRepository<CachedToken, String> {
}