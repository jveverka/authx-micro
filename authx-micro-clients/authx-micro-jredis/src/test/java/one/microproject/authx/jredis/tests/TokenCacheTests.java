package one.microproject.authx.jredis.tests;

import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.TokenType;
import one.microproject.authx.common.utils.CryptoUtils;
import one.microproject.authx.common.utils.TokenUtils;
import one.microproject.authx.jredis.TokenCacheReaderService;
import one.microproject.authx.jredis.TokenCacheWriterService;
import one.microproject.authx.jredis.model.CachedToken;
import one.microproject.authx.jredis.repository.CacheTokenRepository;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Security;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenCacheTests extends AppBaseTest {

    private static final Long TimeToLiveAccess = 10L*1000L;
    private static final Long TimeToLiveRefresh = 20L*1000L;

    @BeforeAll
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Autowired
    TokenCacheReaderService tokenCacheReaderService;

    @Autowired
    TokenCacheWriterService tokenCacheWriterService;

    @Autowired
    CacheTokenRepository cacheTokenRepository;

    @Test
    void testSaveAndVerifyAccessToken() {
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date accessExpiration = new Date(epochMilli + TimeToLiveAccess*1000L);
        KeyPairData keyPairData = generateKeyPair("key-001", "sub", TimeUnit.HOURS, 1L);
        TokenClaims accessClaims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, accessExpiration, TokenType.BEARER, "jti");
        String accessToken = TokenUtils.issueToken(accessClaims, keyPairData.id(), keyPairData.privateKey());
        tokenCacheWriterService.saveAccessToken("p-01", "jti", "NA", accessToken, "key-001", keyPairData.x509Certificate(), TimeToLiveAccess);
        Optional<TokenClaims> verifiedClaims = tokenCacheReaderService.verify("p-01", accessToken);
        assertTrue(verifiedClaims.isPresent());
        tokenCacheWriterService.removeTokenById("p-01", "jti");
        verifiedClaims = tokenCacheReaderService.verify("p-01", accessToken);
        assertTrue(verifiedClaims.isEmpty());
    }

    @Test
    void testSaveAndVerifyRefreshToken() {
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date refreshExpiration = new Date(epochMilli + TimeToLiveRefresh*1000L);
        KeyPairData keyPairData = generateKeyPair("key-001", "sub", TimeUnit.HOURS, 1L);
        TokenClaims refreshClaims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, refreshExpiration, TokenType.REFRESH, "jti");
        String refreshToken = TokenUtils.issueToken(refreshClaims, keyPairData.id(), keyPairData.privateKey());
        tokenCacheWriterService.saveRefreshToken("p-01", "jti", "NA", refreshToken, "key-001", keyPairData.x509Certificate(), TimeToLiveRefresh);
        Optional<TokenClaims> verifiedClaims = tokenCacheReaderService.verify("p-01", refreshToken);
        assertTrue(verifiedClaims.isPresent());
        tokenCacheWriterService.removeTokenById("p-01", "jti");
        verifiedClaims = tokenCacheReaderService.verify("p-01", refreshToken);
        assertTrue(verifiedClaims.isEmpty());
    }

    @Test
    void testRefreshTokenProcedure() {
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date accessExpiration = new Date(epochMilli + TimeToLiveAccess*1000L);
        Date refreshExpiration = new Date(epochMilli + TimeToLiveRefresh*1000L);
        KeyPairData keyPairData = generateKeyPair("key-001", "sub", TimeUnit.HOURS, 1L);

        TokenClaims accessClaims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, accessExpiration, TokenType.BEARER, "access-001");
        TokenClaims refreshClaims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, refreshExpiration, TokenType.REFRESH, "refresh-001");

        String accessToken = TokenUtils.issueToken(accessClaims, keyPairData.id(), keyPairData.privateKey());
        String refreshToken = TokenUtils.issueToken(refreshClaims, keyPairData.id(), keyPairData.privateKey());

        tokenCacheWriterService.saveAccessToken("p-01", "access-001", "refresh-001", accessToken, "key-001", keyPairData.x509Certificate(), TimeToLiveAccess);
        tokenCacheWriterService.saveRefreshToken("p-01", "refresh-001", "access-001", refreshToken, "key-001", keyPairData.x509Certificate(), TimeToLiveRefresh);

        Optional<TokenClaims> verifiedClaims = tokenCacheReaderService.verify("p-01", accessToken);
        assertTrue(verifiedClaims.isPresent());
        verifiedClaims = tokenCacheReaderService.verify("p-01", refreshToken);
        assertTrue(verifiedClaims.isPresent());

        accessClaims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, accessExpiration, TokenType.BEARER, "access-002");
        accessToken = TokenUtils.issueToken(accessClaims, keyPairData.id(), keyPairData.privateKey());
        tokenCacheWriterService.saveRefreshedAccessToken("p-01", "access-002", "refresh-001", accessToken, "key-001", keyPairData.x509Certificate(), TimeToLiveAccess);

        Optional<CachedToken> cachedTokenOptional = cacheTokenRepository.findById("p-01-refresh-001");
        assertTrue(cachedTokenOptional.isPresent());
        cachedTokenOptional = cacheTokenRepository.findById("p-01-access-002");
        assertTrue(cachedTokenOptional.isPresent());
        cachedTokenOptional = cacheTokenRepository.findById("p-01-access-001");
        assertTrue(cachedTokenOptional.isEmpty());

    }

    private KeyPairData generateKeyPair(String id, String subject, TimeUnit unit, Long duration) {
        Instant notBefore = Instant.now();
        return CryptoUtils.generateSelfSignedKeyPair(id, subject, notBefore, unit, duration);
    }

}
