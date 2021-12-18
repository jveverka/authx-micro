package one.microproject.authx.jredis.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import one.microproject.authx.common.dto.TokenType;
import one.microproject.authx.common.utils.TokenUtils;
import one.microproject.authx.jredis.model.CachedToken;
import one.microproject.authx.jredis.repository.CacheTokenRepository;
import one.microproject.authx.jredis.TokenCacheWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.X509Certificate;
import java.util.Optional;

import static one.microproject.authx.common.utils.CryptoUtils.serializeX509Certificate;
import static one.microproject.authx.common.utils.ServiceUtils.createId;
import static one.microproject.authx.common.utils.TokenUtils.JTI_CLAIM;

@Service
@Transactional(readOnly = true)
public class TokenCacheWriterServiceImpl implements TokenCacheWriterService {

    private final CacheTokenRepository cacheTokenRepository;

    @Autowired
    public TokenCacheWriterServiceImpl(CacheTokenRepository cacheTokenRepository) {
        this.cacheTokenRepository = cacheTokenRepository;
    }

    @Override
    @Transactional
    public void saveAccessToken(String projectId, String jti, String refreshJti, String token, String kid, X509Certificate certificate, Long timeToLive) {
        String id = createId(projectId, jti);
        CachedToken cachedToken = new CachedToken(id, token, kid, serializeX509Certificate(certificate), refreshJti, TokenType.BEARER.getType(), timeToLive);
        cacheTokenRepository.save(cachedToken);
    }

    @Override
    @Transactional
    public void saveRefreshToken(String projectId, String jti, String accessJti, String token, String kid, X509Certificate certificate, Long timeToLive) {
        String id = createId(projectId, jti);
        CachedToken cachedToken = new CachedToken(id, token, kid, serializeX509Certificate(certificate), accessJti, TokenType.REFRESH.getType(), timeToLive);
        cacheTokenRepository.save(cachedToken);
    }

    @Override
    @Transactional
    public void saveRefreshedAccessToken(String projectId, String accessJti, String refreshJti, String token, String kid, X509Certificate certificate, Long timeToLive) {
        String accessId = createId(projectId, accessJti);
        String refreshId = createId(projectId, refreshJti);
        Optional<CachedToken> refreshTokenOptional = cacheTokenRepository.findById(refreshId);
        if (refreshTokenOptional.isPresent()) {
            CachedToken refreshToken = refreshTokenOptional.get();
            String accessIdToDelete = createId(projectId, refreshToken.getRelatedTokenId());
            cacheTokenRepository.deleteById(accessIdToDelete);
            refreshToken.setRelatedTokenId(accessJti);
            CachedToken accessToken = new CachedToken(accessId, token, kid, serializeX509Certificate(certificate), refreshJti, TokenType.BEARER.getType(), timeToLive);
            cacheTokenRepository.save(accessToken);
            cacheTokenRepository.save(refreshToken);
        } else {
            throw new TokenCacheException("Refresh token not found !");
        }
    }

    @Override
    @Transactional
    public void removeTokenById(String projectId, String jti) {
        String id = createId(projectId, jti);
        cacheTokenRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeToken(String projectId, String token) {
        Jwt<? extends Header, Claims> jwtToken = TokenUtils.getJwt(token);
        String id = createId(projectId, (String)jwtToken.getBody().get(JTI_CLAIM));
        cacheTokenRepository.deleteById(id);
    }

}
