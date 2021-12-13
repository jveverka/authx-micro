package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.service.repository.CacheTokenRepository;
import one.microproject.authx.service.service.TokenCacheReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenCacheReaderServiceImpl implements TokenCacheReaderService {

    private final CacheTokenRepository cacheTokenRepository;

    @Autowired
    public TokenCacheReaderServiceImpl(CacheTokenRepository cacheTokenRepository) {
        this.cacheTokenRepository = cacheTokenRepository;
    }

    @Override
    public Optional<TokenClaims> verify(String projectId, String jwt) {
        //TODO:
        return Optional.empty();
    }

}
