package one.microproject.authx.service.service.impl.generators;

import one.microproject.authx.common.Constants;
import one.microproject.authx.common.dto.ClientDto;
import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.PermissionDto;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.TokenType;
import one.microproject.authx.common.dto.UserDto;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.common.utils.LabelUtils;
import one.microproject.authx.common.utils.TokenUtils;
import one.microproject.authx.service.dto.GeneratedTokens;
import one.microproject.authx.service.service.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DefaultTokenGenerator implements TokenGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTokenGenerator.class);

    private static final Long DEFAULT_ACCESS_DURATION = 24*60*60*1000L;
    private static final Long DEFAULT_REFRESH_DURATION = 30*24*60*60*1000L;

    @Override
    public GeneratedTokens generateUserTokens(URI issuerUri, ProjectDto project, UserDto user, KeyPairData keyPairData, Set<PermissionDto> permissions, String requestedAudience, Set<String> requestedScopes) {
        LOGGER.info("generateUserTokens: {}", issuerUri);
        Long accessDuration = LabelUtils.getAccessTokenDuration(DEFAULT_ACCESS_DURATION, project.labels(), user.labels());
        Long refreshDuration = LabelUtils.getRefreshTokenDuration(DEFAULT_REFRESH_DURATION, project.labels(), user.labels());

        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date accessExpiration = new Date(epochMilli + accessDuration);
        Date refreshExpiration = new Date(epochMilli + refreshDuration);

        String audience = getAudience(requestedAudience, project);
        Set<String> scopes = getScopes(requestedScopes, permissions);

        String accessJit = UUID.randomUUID().toString();
        String refreshJit = UUID.randomUUID().toString();
        TokenClaims accessClaims = new TokenClaims(issuerUri.toString(), user.id(), audience, scopes, issuedAt, accessExpiration, TokenType.BEARER, accessJit, project.id());
        TokenClaims refreshClaims = new TokenClaims(issuerUri.toString(), user.id(), audience, scopes, issuedAt, refreshExpiration, TokenType.REFRESH, refreshJit, project.id());
        TokenClaims idClaims = new TokenClaims(issuerUri.toString(), user.id(), audience, scopes, issuedAt, accessExpiration, TokenType.ID, UUID.randomUUID().toString(), project.id());
        String accessToken = TokenUtils.issueToken(accessClaims, keyPairData.id(), keyPairData.privateKey());
        String refreshToken = TokenUtils.issueToken(refreshClaims, keyPairData.id(), keyPairData.privateKey());
        String idToken = TokenUtils.issueToken(idClaims, keyPairData.id(), keyPairData.privateKey());
        String tokenType = Constants.BEARER;
        TokenResponse tokenResponse = new TokenResponse(accessToken, (epochMilli + accessDuration), (epochMilli + refreshDuration), refreshToken, tokenType, idToken);
        return new GeneratedTokens(tokenResponse, accessClaims, refreshClaims, accessDuration, refreshDuration);
    }

    @Override
    public GeneratedTokens generateClientTokens(URI issuerUri, ProjectDto project, ClientDto client, KeyPairData keyPairData, Set<PermissionDto> permissions, String requestedAudience, Set<String> requestedScopes) {
        LOGGER.info("generateClientTokens: {}", issuerUri);
        Long accessDuration = LabelUtils.getAccessTokenDuration(DEFAULT_ACCESS_DURATION, project.labels(), client.labels());
        Long refreshDuration = LabelUtils.getRefreshTokenDuration(DEFAULT_REFRESH_DURATION, project.labels(), client.labels());

        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date accessExpiration = new Date(epochMilli + accessDuration);
        Date refreshExpiration = new Date(epochMilli + refreshDuration);

        String audience = getAudience(requestedAudience, project);
        Set<String> scopes = getScopes(requestedScopes, permissions);

        String accessJit = UUID.randomUUID().toString();
        String refreshJit = UUID.randomUUID().toString();

        TokenClaims accessClaims = new TokenClaims(issuerUri.toString(), client.id(), audience, scopes, issuedAt, accessExpiration, TokenType.BEARER, accessJit, project.id());
        TokenClaims refreshClaims = new TokenClaims(issuerUri.toString(), client.id(), audience, scopes, issuedAt, refreshExpiration, TokenType.REFRESH, refreshJit, project.id());
        TokenClaims idClaims = new TokenClaims(issuerUri.toString(), client.id(), audience, scopes, issuedAt, accessExpiration, TokenType.ID, UUID.randomUUID().toString(), project.id());
        String accessToken = TokenUtils.issueToken(accessClaims, keyPairData.id(), keyPairData.privateKey());
        String refreshToken = TokenUtils.issueToken(refreshClaims, keyPairData.id(), keyPairData.privateKey());
        String idToken = TokenUtils.issueToken(idClaims, keyPairData.id(), keyPairData.privateKey());
        String tokenType = Constants.BEARER;
        TokenResponse tokenResponse = new TokenResponse(accessToken, (epochMilli + accessDuration), (epochMilli + refreshDuration), refreshToken, tokenType, idToken);
        return new GeneratedTokens(tokenResponse, accessClaims, refreshClaims, accessDuration, refreshDuration);
    }

    @Override
    public GeneratedTokens refreshTokens(ProjectDto project, UserDto user, KeyPairData keyPairData, Set<PermissionDto> permissions, TokenClaims refreshClaims, String refreshToken) {
        LOGGER.info("refreshTokens user: {}", project.id());
        Long accessDuration = LabelUtils.getAccessTokenDuration(DEFAULT_ACCESS_DURATION, project.labels(), user.labels());
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date accessExpiration = new Date(epochMilli + accessDuration);
        String accessJti = UUID.randomUUID().toString();
        TokenClaims accessClaims = new TokenClaims(refreshClaims.issuer(), refreshClaims.subject(), refreshClaims.audience(), refreshClaims.scope(), issuedAt, accessExpiration, TokenType.BEARER, accessJti, refreshClaims.projectId());

        String accessToken = TokenUtils.issueToken(accessClaims, keyPairData.id(), keyPairData.privateKey());
        String tokenType = Constants.BEARER;
        TokenResponse tokenResponse = new TokenResponse(accessToken, (epochMilli + accessDuration), refreshClaims.expiration().getTime(), refreshToken, tokenType, null);
        return new GeneratedTokens(tokenResponse, accessClaims, refreshClaims, accessDuration, null);
    }

    @Override
    public GeneratedTokens refreshTokens(ProjectDto project, ClientDto client, KeyPairData keyPairData, Set<PermissionDto> permissions, TokenClaims refreshClaims, String refreshToken) {
        LOGGER.info("refreshTokens client: {}", project.id());
        Long accessDuration = LabelUtils.getAccessTokenDuration(DEFAULT_ACCESS_DURATION, project.labels(), client.labels());
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date accessExpiration = new Date(epochMilli + accessDuration);
        String accessJti = UUID.randomUUID().toString();
        TokenClaims accessClaims = new TokenClaims(refreshClaims.issuer(), refreshClaims.subject(), refreshClaims.audience(), refreshClaims.scope(), issuedAt, accessExpiration, TokenType.BEARER, accessJti, refreshClaims.projectId());

        String accessToken = TokenUtils.issueToken(accessClaims, keyPairData.id(), keyPairData.privateKey());
        String tokenType = Constants.BEARER;
        TokenResponse tokenResponse = new TokenResponse(accessToken, (epochMilli + accessDuration), refreshClaims.expiration().getTime(), refreshToken, tokenType, null);
        return new GeneratedTokens(tokenResponse, accessClaims, refreshClaims, accessDuration, null);

    }

    private String getAudience(String requestedAudience, ProjectDto project) {
        return project.id();
    }

    private Set<String> getScopes(Set<String> requestedScopes, Set<PermissionDto> permissions) {
        Set<String> result = new HashSet<>();
        if (permissions != null) {
            permissions.forEach(p -> {
                String permission = p.resource() + "." + p.service() + "." + p.action();
                result.add(permission);
            });
        }
        if (requestedScopes != null) {
            if (!requestedScopes.isEmpty()) {
                //TODO: add scope filtering
            }
        }
        return result;
    }

}
