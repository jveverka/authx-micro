package one.microproject.authx.common.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
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
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenUtilTest {

    private static final Long ONE_HOUR_MILLIS = 60*60*1000L;
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    void testIssueAndValidateToken() {
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date expiration = new Date(epochMilli + 10*1000L);
        KeyPairData keyPairData = CryptoUtils.generateSelfSignedKeyPair("kid-001", "iss", Instant.now(), TimeUnit.MINUTES, 10L);
        TokenClaims tokenClaims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, expiration, TokenType.BEARER, "jti");
        String token = TokenUtils.issueToken(tokenClaims, keyPairData.id(), keyPairData.privateKey());
        TokenClaims claims = TokenUtils.validate(token, keyPairData.x509Certificate());
        assertNotNull(claims);
        assertEquals(tokenClaims, claims);
    }

    @Test
    void testIssueAndValidateExpiredToken() {
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli - ONE_HOUR_MILLIS);
        Date expiration = new Date(epochMilli + 10*1000L - ONE_HOUR_MILLIS);
        KeyPairData keyPairData = CryptoUtils.generateSelfSignedKeyPair("kid-001", "iss", Instant.now(), TimeUnit.MINUTES, 10L);
        TokenClaims tokenClaims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, expiration, TokenType.BEARER, "jti");
        String token = TokenUtils.issueToken(tokenClaims, keyPairData.id(), keyPairData.privateKey());
        assertThrows(ExpiredJwtException.class, () -> TokenUtils.validate(token, keyPairData.x509Certificate()));
    }

    @Test
    void testIssueAndValidateTokenWrongKey() {
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date expiration = new Date(epochMilli + 10*1000L);
        KeyPairData keyPairData01 = CryptoUtils.generateSelfSignedKeyPair("kid-001", "iss", Instant.now(), TimeUnit.MINUTES, 10L);
        KeyPairData keyPairData02 = CryptoUtils.generateSelfSignedKeyPair("kid-001", "iss", Instant.now(), TimeUnit.MINUTES, 10L);
        TokenClaims tokenClaims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, expiration, TokenType.BEARER, "jti");
        String token = TokenUtils.issueToken(tokenClaims, keyPairData01.id(), keyPairData01.privateKey());
        assertThrows(SignatureException.class, () -> TokenUtils.validate(token, keyPairData02.x509Certificate()));
    }

    @Test
    void testIssueAndValidateTokenMissingSignature() {
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date expiration = new Date(epochMilli + 10*1000L);
        KeyPairData keyPairData = CryptoUtils.generateSelfSignedKeyPair("kid-001", "iss", Instant.now(), TimeUnit.MINUTES, 10L);
        TokenClaims tokenClaims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, expiration, TokenType.BEARER, "jti");
        String token = TokenUtils.issueToken(tokenClaims, keyPairData.id(), keyPairData.privateKey());
        String[] tokenParts = token.split("\\.");
        String tokenNoSignature = tokenParts[0] + "." + tokenParts[1];
        assertThrows(MalformedJwtException.class, () -> TokenUtils.validate(tokenNoSignature, keyPairData.x509Certificate()));
    }

    @Test
    void testIssueAndValidateTokenTamperedHeader() {
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date expiration = new Date(epochMilli + 10*1000L);
        KeyPairData keyPairData = CryptoUtils.generateSelfSignedKeyPair("kid-001", "iss", Instant.now(), TimeUnit.MINUTES, 10L);
        TokenClaims tokenClaims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, expiration, TokenType.BEARER, "jti");
        String token = TokenUtils.issueToken(tokenClaims, keyPairData.id(), keyPairData.privateKey());
        String[] tokenParts = token.split("\\.");
        String decodedHeader = new String(Base64.getDecoder().decode(tokenParts[0]));
        String newHeader = decodedHeader.replace("RS256", "none");
        String newEncodedHeader = Base64.getEncoder().withoutPadding().encodeToString(newHeader.getBytes());
        String newToken = newEncodedHeader + "." + tokenParts[1] + "." + tokenParts[2];
        assertThrows(MalformedJwtException.class, () -> TokenUtils.validate(newToken, keyPairData.x509Certificate()));
    }

}
