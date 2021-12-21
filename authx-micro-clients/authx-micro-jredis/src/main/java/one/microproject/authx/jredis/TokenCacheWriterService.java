package one.microproject.authx.jredis;

import java.security.cert.X509Certificate;

public interface TokenCacheWriterService {

    /**
     * Save JWT access token and corresponding X509 certificate in cache by it unique ProjectId and JTI.
     * @param projectId - unique project ID.
     * @param jti - unique ID of JWT Token.
     * @param refreshJti - jit of related refresh token.
     * @param token - JWT access token.
     * @param certificate - X509 certificate for token signature verification.
     * @param timeToLive - token expiration interval in seconds.
     */
    void saveAccessToken(String projectId, String jti, String refreshJti, String token, X509Certificate certificate, Long timeToLive);

    /**
     * Save JWT refresh token and corresponding X509 certificate in cache by it unique ProjectId and JTI.
     * @param projectId - unique project ID.
     * @param jti - unique ID of JWT Token.
     * @param accessJti - jit of related access token.
     * @param token - JWT refresh token.
     * @param certificate - X509 certificate for token signature verification.
     * @param timeToLive - token expiration interval in seconds.
     */
    void saveRefreshToken(String projectId, String jti, String accessJti, String token, X509Certificate certificate, Long timeToLive);

    /**
     * Save new Access Token after successful refresh,
     * @param projectId - unique project ID.
     * @param jti - unique ID of refreshed access JWT Token.
     * @param refreshJti - unique ID of related refresh JWT Token.
     * @param token - new JWT access token.
     * @param certificate - X509 certificate for token signature verification.
     * @param timeToLive - token expiration interval in seconds.
     */
    void saveRefreshedAccessToken(String projectId, String jti, String refreshJti, String token, X509Certificate certificate, Long timeToLive);

    /**
     * Remove JWT Token by it's unique ID.
     * @param projectId - unique project ID.
     * @param jti - unique ID of JWT Token.
     */
    void removeTokenById(String projectId, String jti);

    /**
     * Remove JWT Token by it's unique ID.
     * @param projectId - unique project ID.
     * @param token - JWT token to remove.
     */
    void removeToken(String projectId, String token);

}
