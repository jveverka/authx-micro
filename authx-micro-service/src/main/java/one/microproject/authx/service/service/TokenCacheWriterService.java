package one.microproject.authx.service.service;

import java.security.cert.X509Certificate;

public interface TokenCacheWriterService {

    /**
     * Save JWT token and corresponding X509 certificate in cache by it unique ProjectId and JTI.
     * @param projectId - unique project ID.
     * @param jti - unique ID of JWT Token.
     * @param token - JWT token.
     * @param kid - unique Key ID used to verify token's signature.
     * @param certificate - X509 certificate for token signature verification.
     */
    void saveIssuedToken(String projectId, String jti, String token, String kid, X509Certificate certificate);

    /**
     * Remove JWT Token by it's unique ID.
     * @param projectId - unique project ID.
     * @param jti - unique ID of JWT Token.
     */
    void removeToken(String projectId, String jti);

}