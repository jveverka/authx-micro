package one.microproject.authx.service.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.utils.CryptoUtils;
import one.microproject.authx.common.utils.TokenUtils;
import one.microproject.authx.service.model.CachedToken;
import one.microproject.authx.service.repository.CacheTokenRepository;
import one.microproject.authx.service.service.TokenCacheReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.X509Certificate;
import java.util.Optional;

import static one.microproject.authx.common.utils.ServiceUtils.createId;
import static one.microproject.authx.common.utils.TokenUtils.JTI_CLAIM;

@Service
@Transactional(readOnly = true)
public class TokenCacheReaderServiceImpl implements TokenCacheReaderService {

    private final CacheTokenRepository cacheTokenRepository;

    @Autowired
    public TokenCacheReaderServiceImpl(CacheTokenRepository cacheTokenRepository) {
        this.cacheTokenRepository = cacheTokenRepository;
    }

    @Override
    public Optional<TokenClaims> verify(String projectId, String jwt) {
        Jwt<? extends Header, Claims> jwtToken = TokenUtils.getJwt(jwt);
        String id = createId(projectId, (String)jwtToken.getBody().get(JTI_CLAIM));
        Optional<CachedToken> cachedTokenOptional = cacheTokenRepository.findById(id);
        if (cachedTokenOptional.isPresent()) {
            X509Certificate x509Certificate = CryptoUtils.deserializeX509Certificate(cachedTokenOptional.get().getX509Certificate());
            return Optional.of(TokenUtils.validate(jwt, x509Certificate));
        } else {
            return Optional.empty();
        }
    }

}
