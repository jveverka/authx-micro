package one.microproject.authx.common.tests;

import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.TokenType;
import one.microproject.authx.common.utils.CryptoUtils;
import one.microproject.authx.common.utils.TokenUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.Security;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TokenUtilTest {

    @BeforeAll
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    void testIssueAndValidateToken() {
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date expiration = new Date(epochMilli + 10*1000L);
        KeyPairData keyPairData = CryptoUtils.generateSelfSignedKeyPair("kid-001", "iss", Instant.now(), TimeUnit.MINUTES, 1L);
        TokenClaims tokenClaims = new TokenClaims("iss", "sub", "aud", issuedAt, expiration, TokenType.BEARER);
        String token = TokenUtils.issueToken(tokenClaims, keyPairData.id(), keyPairData.privateKey());
        TokenClaims claims = TokenUtils.validate(token, keyPairData.x509Certificate());
        assertNotNull(claims);
        assertEquals(tokenClaims, claims);
    }

}
