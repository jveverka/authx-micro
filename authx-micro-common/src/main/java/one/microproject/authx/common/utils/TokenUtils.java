package one.microproject.authx.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.TokenType;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;

public final class TokenUtils {

    public static final String KEY_ID = "kid";
    public static final String TYP_ID = "typ";
    public static final String TYP_VALUE = "JWT";
    public static final String TYPE_CLAIM = "typ";

    private TokenUtils() {
    }

    public static String issueToken(TokenClaims claims, String keyId, PrivateKey privateKey) {
        JwtBuilder builder = Jwts.builder();
        builder.setHeaderParam(TYP_ID, TYP_VALUE);
        builder.setHeaderParam(KEY_ID, keyId);
        builder.setIssuer(claims.issuer());
        builder.setSubject(claims.subject());
        builder.setAudience(claims.audience());
        builder.setExpiration(claims.expiration());
        builder.setIssuedAt(claims.issuedAt());
        builder.claim(TYPE_CLAIM, claims.type().getType());
        builder.signWith(privateKey);
        return builder.compact();
    }

    public static TokenClaims validate(String token, X509Certificate certificate) {
        Jwt<? extends Header, Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(certificate.getPublicKey())
                .build()
                .parseClaimsJws(token);
        Claims claims = jwt.getBody();
        String iss = claims.getIssuer();
        String sub = claims.getSubject();
        String aud = claims.getAudience();
        String tokenType = (String) claims.get("typ");
        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();
        return new TokenClaims(iss, sub, aud, issuedAt, expiration, TokenType.getTokenType(tokenType));
    }

}
