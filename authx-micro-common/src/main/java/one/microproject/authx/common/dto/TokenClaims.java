package one.microproject.authx.common.dto;

import java.util.Date;
import java.util.Set;

public record TokenClaims(String issuer, String subject, String audience, Set<String> scope, Date issuedAt, Date expiration, TokenType type) {
}
