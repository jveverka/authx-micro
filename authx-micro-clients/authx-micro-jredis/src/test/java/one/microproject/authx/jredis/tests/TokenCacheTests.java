package one.microproject.authx.jredis.tests;

import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.TokenType;
import one.microproject.authx.common.utils.CryptoUtils;
import one.microproject.authx.common.utils.TokenUtils;
import one.microproject.authx.jredis.TokenCacheReaderService;
import one.microproject.authx.jredis.TokenCacheWriterService;
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

    @BeforeAll
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Autowired
    TokenCacheReaderService tokenCacheReaderService;

    @Autowired
    TokenCacheWriterService tokenCacheWriterService;

    @Test
    void testSaveAndVerifyToken() {
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date expiration = new Date(epochMilli + 10*1000L);
        KeyPairData keyPairData = generateKeyPair("key-001", "sub", TimeUnit.HOURS, 1L);
        TokenClaims claims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, expiration, TokenType.BEARER, "jti");
        String token = TokenUtils.issueToken(claims, keyPairData.id(), keyPairData.privateKey());
        tokenCacheWriterService.saveToken("p-01", "jti", token, "key-001", keyPairData.x509Certificate());
        Optional<TokenClaims> verifiedClaims = tokenCacheReaderService.verify("p-01", token);
        assertTrue(verifiedClaims.isPresent());
        tokenCacheWriterService.removeToken("p-01", "jti");
        verifiedClaims = tokenCacheReaderService.verify("p-01", token);
        assertTrue(verifiedClaims.isEmpty());
    }

    private KeyPairData generateKeyPair(String id, String subject, TimeUnit unit, Long duration) {
        Instant notBefore = Instant.now();
        return CryptoUtils.generateSelfSignedKeyPair(id, subject, notBefore, unit, duration);
    }

}
