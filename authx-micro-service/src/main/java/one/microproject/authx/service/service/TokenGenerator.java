package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.ClientDto;
import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.PermissionDto;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.UserDto;
import one.microproject.authx.service.dto.GeneratedTokens;

import java.net.URI;
import java.util.Set;

/**
 * Generate tokens from application context.
 */
public interface TokenGenerator {

    GeneratedTokens generateUserTokens(URI issuerUri, ProjectDto project, UserDto user, KeyPairData keyPairData, Set<PermissionDto> permissions, String requestedAudience, Set<String> requestedScopes);

    GeneratedTokens generateClientTokens(URI issuerUri, ProjectDto project, ClientDto client, KeyPairData keyPairData, Set<PermissionDto> permissions, String requestedAudience, Set<String> requestedScopes);

    GeneratedTokens refreshTokens(ProjectDto project, UserDto user, KeyPairData keyPairData, Set<PermissionDto> permissions, TokenClaims refreshClaims, String refreshToken);

    GeneratedTokens refreshTokens(ProjectDto project, ClientDto client, KeyPairData keyPairData, Set<PermissionDto> permissions, TokenClaims refreshClaims, String refreshToken);

}
