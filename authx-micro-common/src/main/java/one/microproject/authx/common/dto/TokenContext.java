package one.microproject.authx.common.dto;

public record TokenContext(TokenClaims tokenClaims, GrantType type) {
}
