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
import java.util.HashSet;
import java.util.Set;

public final class TokenUtils {

    public static final String KEY_ID = "kid";
    public static final String TYP_ID = "typ";
    public static final String TYP_VALUE = "JWT";
    public static final String TYPE_CLAIM = "typ";
    public static final String SCOPE_CLAIM = "scope";
    public static final String JTI_CLAIM = "jti";

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
        builder.claim(SCOPE_CLAIM, mapScopes(claims.scope()));
        builder.claim(JTI_CLAIM, claims.jti());
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
        String tokenType = (String) claims.get(TYPE_CLAIM);
        String scopes = (String) claims.get(SCOPE_CLAIM);
        String jti = (String) claims.get(JTI_CLAIM);
        Set<String> scope = mapScopes(scopes);
        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();
        return new TokenClaims(iss, sub, aud, scope, issuedAt, expiration, TokenType.getTokenType(tokenType), jti);
    }

    public static Set<String> mapScopes(String scopes) {
        if (scopes == null) {
            return Set.of();
        }
        if (scopes.trim().length() == 0) {
            return Set.of();
        }
        String[] s = scopes.trim().split(" ");
        Set<String> scope = new HashSet<>();
        for (String sc: s) {
            scope.add(sc);
        }
        return scope;
    }

    public static String mapScopes(Set<String> scope) {
        if (scope == null) {
            return "";
        }
        if (scope.isEmpty()) {
            return "";
        }
        return String.join(" ", scope);
    }

}
