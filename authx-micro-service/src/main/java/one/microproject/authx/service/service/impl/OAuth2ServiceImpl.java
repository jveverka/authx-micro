package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.Constants;
import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.PermissionDto;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.TokenType;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.UserDto;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.JWKResponse;
import one.microproject.authx.common.dto.oauth2.ProviderConfigurationResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.common.dto.oauth2.UserInfoResponse;
import one.microproject.authx.common.utils.LabelUtils;
import one.microproject.authx.common.utils.TokenUtils;
import one.microproject.authx.jredis.TokenCacheReaderService;
import one.microproject.authx.jredis.TokenCacheWriterService;
import one.microproject.authx.service.exceptions.OAuth2TokenException;
import one.microproject.authx.service.service.ClientService;
import one.microproject.authx.service.service.OAuth2Service;
import one.microproject.authx.service.service.PermissionService;
import one.microproject.authx.service.service.ProjectService;
import one.microproject.authx.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ServiceImpl.class);

    private static final Long DEFAULT_ACCESS_DURATION = 24*60*60*1000L;
    private static final Long DEFAULT_REFRESH_DURATION = 30*24*60*60*1000L;

    private final ProjectService projectService;
    private final UserService userService;
    private final ClientService clientService;
    private final PermissionService permissionService;

    private final TokenCacheReaderService tokenCacheReaderService;
    private final TokenCacheWriterService tokenCacheWriterService;

    @Autowired
    public OAuth2ServiceImpl(ProjectService projectService, UserService userService, ClientService clientService, PermissionService permissionService,
                             TokenCacheReaderService tokenCacheReaderService, TokenCacheWriterService tokenCacheWriterService) {
        this.projectService = projectService;
        this.userService = userService;
        this.clientService = clientService;
        this.permissionService = permissionService;
        this.tokenCacheReaderService = tokenCacheReaderService;
        this.tokenCacheWriterService = tokenCacheWriterService;
    }

    @Override
    public TokenResponse getTokenForPassword(URI issuerUri, String projectId, ClientCredentials clientCredentials, String requestedAudience, Set<String> requestedScopes, UserCredentials userCredentials) {
        LOGGER.info("getTokenForPassword: {} {} {} {}", issuerUri, projectId, clientCredentials.id(), userCredentials.username());
        Optional<ProjectDto> projectDto = projectService.get(projectId);
        if (projectDto.isEmpty()) {
            LOGGER.warn("Project not found {}", projectId);
            throw new OAuth2TokenException("Project not found !");
        }
        Boolean clientOk = clientService.verifySecret(projectId, clientCredentials.id(), clientCredentials.secret());
        Boolean userOk = userService.verifySecret(projectId, userCredentials.username(), userCredentials.password());
        if (clientOk && userOk) {
            Optional<UserDto> userDto = userService.get(projectId, userCredentials.username());
            Optional<KeyPairData> keyPairDataOptional = userService.getDefaultKeyPair(projectId, userCredentials.username());
            if (userDto.isPresent() && keyPairDataOptional.isPresent()) {
                LOGGER.info("Generating tokens for {} {} {} {}", issuerUri, projectId, clientCredentials.id(), userCredentials.username());
                ProjectDto project = projectDto.get();
                UserDto user = userDto.get();
                KeyPairData keyPairData = keyPairDataOptional.get();

                Set<PermissionDto> permissions = permissionService.getPermissions(user.roles());

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
                TokenClaims accessClaims = new TokenClaims(issuerUri.toString(), user.id(), audience, scopes, issuedAt, accessExpiration, TokenType.BEARER, accessJit, projectId);
                TokenClaims refreshClaims = new TokenClaims(issuerUri.toString(), user.id(), audience, scopes, issuedAt, refreshExpiration, TokenType.REFRESH, refreshJit, projectId);
                TokenClaims idClaims = new TokenClaims(issuerUri.toString(), user.id(), audience, scopes, issuedAt, accessExpiration, TokenType.ID, UUID.randomUUID().toString(), projectId);
                String accessToken = TokenUtils.issueToken(accessClaims, keyPairData.id(), keyPairData.privateKey());
                String refreshToken = TokenUtils.issueToken(refreshClaims, keyPairData.id(), keyPairData.privateKey());
                String idToken = TokenUtils.issueToken(idClaims, keyPairData.id(), keyPairData.privateKey());
                String tokenType = Constants.BEARER;
                tokenCacheWriterService.saveAccessToken(projectId, accessClaims.jti(), refreshJit, accessToken, keyPairData.x509Certificate(), accessDuration);
                tokenCacheWriterService.saveRefreshToken(projectId, refreshClaims.jti(), accessJit, refreshToken, keyPairData.x509Certificate(), refreshDuration);
                return new TokenResponse(accessToken, (epochMilli + accessDuration), (epochMilli + refreshDuration), refreshToken, tokenType, idToken);
            } else {
                LOGGER.warn("User not found: {}", userCredentials.username());
                throw new OAuth2TokenException("User not found !");
            }
        } else {
            LOGGER.warn("Not Authorized or Not Found: {} {}", clientOk, userOk);
            throw new OAuth2TokenException("Not Authorized or Not Found !");
        }
    }

    @Override
    public TokenResponse getTokenForClientCredentials(URI issuerUri, String projectId, ClientCredentials clientCredentials, String requestedAudience, Set<String> requestedScopes) {
        LOGGER.info("getTokenForClientCredentials: {} {}", issuerUri, projectId);
        Optional<ProjectDto> projectDto = projectService.get(projectId);
        if (projectDto.isEmpty()) {
            throw new OAuth2TokenException("Project not found !");
        }
        Boolean clientOk = clientService.verifySecret(projectId, clientCredentials.id(), clientCredentials.secret());
        if (clientOk) {
            throw new UnsupportedOperationException("Not implemented yet !");
        } else {
            throw new OAuth2TokenException("Not Authorized or Not Found !");
        }
    }

    @Override
    public TokenResponse getTokenForRefreshToken(String projectId, String refreshToken, ClientCredentials clientCredentials) {
        LOGGER.info("getTokenForRefreshToken: {}", projectId);
        Optional<ProjectDto> projectDto = projectService.get(projectId);
        if (projectDto.isEmpty()) {
            LOGGER.warn("Project not found {}", projectId);
            throw new OAuth2TokenException("Project not found !");
        }
        Optional<TokenClaims> claimsOptional = tokenCacheReaderService.verify(refreshToken, TokenType.REFRESH.getType());
        if (claimsOptional.isPresent()) {
            TokenClaims refreshClaims = claimsOptional.get();
            Optional<UserDto> userDto = userService.get(projectId, refreshClaims.subject());
            Optional<KeyPairData> keyPairDataOptional = userService.getDefaultKeyPair(projectId, refreshClaims.subject());
            if (userDto.isPresent() && keyPairDataOptional.isPresent()) {
                ProjectDto project = projectDto.get();
                UserDto user = userDto.get();
                Boolean result = clientService.verifySecret(projectId, clientCredentials.id(), clientCredentials.secret());
                if (!result) {
                    throw new OAuth2TokenException("Not Authorized or Not Found !");
                }

                KeyPairData keyPairData = keyPairDataOptional.get();
                Long accessDuration = LabelUtils.getAccessTokenDuration(DEFAULT_ACCESS_DURATION, project.labels(), user.labels());
                Long epochMilli = Instant.now().getEpochSecond() * 1000L;
                Date issuedAt = new Date(epochMilli);
                Date accessExpiration = new Date(epochMilli + accessDuration);
                String accessJti = UUID.randomUUID().toString();
                TokenClaims accessClaims = new TokenClaims(refreshClaims.issuer(), refreshClaims.subject(), refreshClaims.audience(), refreshClaims.scope(), issuedAt, accessExpiration, TokenType.BEARER, accessJti, refreshClaims.projectId());

                String accessToken = TokenUtils.issueToken(accessClaims, keyPairData.id(), keyPairData.privateKey());
                tokenCacheWriterService.saveRefreshedAccessToken(projectId, accessClaims.jti(), refreshClaims.jti(), accessToken, keyPairData.x509Certificate(), accessDuration);
                String tokenType = Constants.BEARER;
                return new TokenResponse(accessToken, (epochMilli + accessDuration), refreshClaims.expiration().getTime(), refreshToken, tokenType, null);
            } else {
                throw new OAuth2TokenException("Not Authorized or Not Found !");
            }
        } else {
            throw new OAuth2TokenException("Not Authorized or Not Found !");
        }
    }

    @Override
    public ProviderConfigurationResponse getProviderConfiguration(URI issuerUri, String projectId) {
        //TODO: finish implementation
        throw new UnsupportedOperationException("Not implemented yet !");
    }

    @Override
    public JWKResponse getJWKResponse(String projectId) {
        //TODO: finish implementation
        throw new UnsupportedOperationException("Not implemented yet !");
    }

    @Override
    public IntrospectResponse getIntrospectResponse(String projectId, String token, String tokenTypeHint) {
        //TODO: use client Id and secret for request validation (https://datatracker.ietf.org/doc/html/rfc7662)
        LOGGER.info("getIntrospectResponse: {} {}", projectId, tokenTypeHint);
        Optional<TokenClaims> claimsOptional = tokenCacheReaderService.verify(token, tokenTypeHint);
        if (claimsOptional.isPresent()) {
            return new IntrospectResponse(Boolean.TRUE);
        } else {
            return new IntrospectResponse(Boolean.FALSE);
        }
    }

    @Override
    public void revoke(String projectId, String token, String tokenTypeHint) {
        //TODO: use client Id and secret for request validation (https://datatracker.ietf.org/doc/html/rfc7009)
        LOGGER.info("revoke: {} {}", projectId, tokenTypeHint);
        Optional<TokenClaims> tokenClaims = tokenCacheReaderService.verify(token, tokenTypeHint);
        if (tokenClaims.isPresent()) {
            tokenCacheWriterService.removeToken(token);
            return;
        } else {
            throw new OAuth2TokenException("Token Revoke Error !");
        }
    }

    @Override
    public Optional<UserInfoResponse> getUserInfo(String projectId, String token) {
        Optional<TokenClaims> tokenClaims = tokenCacheReaderService.verify(token);
        if (tokenClaims.isPresent()) {
            TokenClaims claims = tokenClaims.get();
            return Optional.of(new UserInfoResponse(claims.subject()));
        } else {
            throw new OAuth2TokenException("Not Authorized or Not Found !");
        }
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
