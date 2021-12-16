package one.microproject.authx.jredis.repository;

import one.microproject.authx.jredis.model.CachedToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheTokenRepository extends CrudRepository<CachedToken, String> {
}