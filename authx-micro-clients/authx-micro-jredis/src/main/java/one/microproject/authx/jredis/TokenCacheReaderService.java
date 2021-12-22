package one.microproject.authx.jredis;

import one.microproject.authx.common.dto.TokenClaims;

import java.util.Optional;

public interface TokenCacheReaderService {

    /**
     * Verify validity of JWT Token. Token is valid if it's signature is valid and is not expired.
     * @param jwt - JWT token to verify.
     * @return
     */
    Optional<TokenClaims> verify(String jwt);

    /**
     * Verify validity of JWT Token. Token is valid if it's signature is valid and is not expired.
     * @param jwt - JWT token to verify.
     * @param tokenTypeHint - expected token type.
     * @return
     */
    Optional<TokenClaims> verify(String jwt, String tokenTypeHint);

}
