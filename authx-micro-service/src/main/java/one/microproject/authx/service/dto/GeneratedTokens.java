package one.microproject.authx.service.dto;

import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.oauth2.TokenResponse;

public record GeneratedTokens(TokenResponse tokenResponse, TokenClaims accessClaims, TokenClaims refreshClaims, Long accessDuration, Long refreshDuration) {
}
