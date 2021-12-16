package one.microproject.authx.jredis;

import one.microproject.authx.common.dto.TokenClaims;

import java.util.Optional;

public interface TokenCacheReaderService {

    /**
     * Verify
     * @param projectId - unique project ID.
     * @param jwt - JWT token to verify.
     * @return
     */
    Optional<TokenClaims> verify(String projectId, String jwt);

}
