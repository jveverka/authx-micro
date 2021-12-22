package one.microproject.authx.jredis.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.utils.CryptoUtils;
import one.microproject.authx.common.utils.TokenUtils;
import one.microproject.authx.jredis.TokenCacheReaderService;
import one.microproject.authx.jredis.model.CachedToken;
import one.microproject.authx.jredis.repository.CacheTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.X509Certificate;
import java.util.Optional;

import static one.microproject.authx.common.utils.TokenUtils.JTI_CLAIM;
import static one.microproject.authx.common.utils.TokenUtils.TYP_ID;

@Service
@Transactional(readOnly = true)
public class TokenCacheReaderServiceImpl implements TokenCacheReaderService {

    private final CacheTokenRepository cacheTokenRepository;

    @Autowired
    public TokenCacheReaderServiceImpl(CacheTokenRepository cacheTokenRepository) {
        this.cacheTokenRepository = cacheTokenRepository;
    }

    @Override
    public Optional<TokenClaims> verify(String jwt) {
        return verify(jwt, null);
    }

    @Override
    public Optional<TokenClaims> verify(String jwt, String tokenTypeHint) {
        Jwt<? extends Header, Claims> jwtToken = TokenUtils.getJwt(jwt);
        if (tokenTypeHint != null && tokenTypeHint.length() > 0 && !jwtToken.getBody().get(TYP_ID).equals(tokenTypeHint)) {
            return Optional.empty();
        }
        String id = (String)jwtToken.getBody().get(JTI_CLAIM);
        Optional<CachedToken> cachedTokenOptional = cacheTokenRepository.findById(id);
        if (cachedTokenOptional.isPresent()) {
            X509Certificate x509Certificate = CryptoUtils.deserializeX509Certificate(cachedTokenOptional.get().getX509Certificate());
            return Optional.of(TokenUtils.validate(jwt, x509Certificate));
        } else {
            return Optional.empty();
        }
    }

}
