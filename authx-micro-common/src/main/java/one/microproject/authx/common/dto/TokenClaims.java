package one.microproject.authx.common.dto;

import java.util.Date;

public record TokenClaims(String issuer, String subject, String audience, Date issuedAt, Date expiration, TokenType type) {
}
