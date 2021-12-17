package one.microproject.authx.jredis.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import one.microproject.authx.common.utils.TokenUtils;
import one.microproject.authx.jredis.model.CachedToken;
import one.microproject.authx.jredis.repository.CacheTokenRepository;
import one.microproject.authx.jredis.TokenCacheWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.X509Certificate;

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
    public void saveToken(String projectId, String jti, String token, String kid, X509Certificate certificate) {
        String id = createId(projectId, jti);
        CachedToken cachedToken = new CachedToken(id, token, kid, serializeX509Certificate(certificate));
        cacheTokenRepository.save(cachedToken);
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
