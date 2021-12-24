package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.ClientDto;
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
import one.microproject.authx.jredis.TokenCacheReaderService;
import one.microproject.authx.jredis.TokenCacheWriterService;
import one.microproject.authx.service.dto.GeneratedTokens;
import one.microproject.authx.service.exceptions.OAuth2TokenException;
import one.microproject.authx.service.service.ClientService;
import one.microproject.authx.service.service.OAuth2Service;
import one.microproject.authx.service.service.PermissionService;
import one.microproject.authx.service.service.ProjectService;
import one.microproject.authx.service.service.TokenGenerator;
import one.microproject.authx.service.service.UserService;
import one.microproject.authx.service.service.impl.generators.DefaultTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ServiceImpl.class);

    private static final String DEFAULT = "default";

    private final ProjectService projectService;
    private final UserService userService;
    private final ClientService clientService;
    private final PermissionService permissionService;

    private final TokenCacheReaderService tokenCacheReaderService;
    private final TokenCacheWriterService tokenCacheWriterService;

    private final Map<String, TokenGenerator> tokenGenerators;

    @Autowired
    public OAuth2ServiceImpl(ProjectService projectService, UserService userService, ClientService clientService, PermissionService permissionService,
                             TokenCacheReaderService tokenCacheReaderService, TokenCacheWriterService tokenCacheWriterService) {
        this.projectService = projectService;
        this.userService = userService;
        this.clientService = clientService;
        this.permissionService = permissionService;
        this.tokenCacheReaderService = tokenCacheReaderService;
        this.tokenCacheWriterService = tokenCacheWriterService;
        this.tokenGenerators = new ConcurrentHashMap<>();
        this.tokenGenerators.put(DEFAULT, new DefaultTokenGenerator());
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
                LOGGER.info("Generating user tokens for {} {} {} {}", issuerUri, projectId, clientCredentials.id(), userCredentials.username());
                ProjectDto project = projectDto.get();
                UserDto user = userDto.get();
                KeyPairData keyPairData = keyPairDataOptional.get();
                Set<PermissionDto> permissions = permissionService.getPermissions(user.roles());

                TokenGenerator tokenGenerator = tokenGenerators.get(DEFAULT);
                GeneratedTokens generatedTokens = tokenGenerator.generateUserTokens(issuerUri, project, user, keyPairData, permissions, requestedAudience, requestedScopes);

                tokenCacheWriterService.saveAccessToken(projectId, generatedTokens.accessClaims().jti(),
                        generatedTokens.refreshClaims().jti(), generatedTokens.tokenResponse().getAccessToken(),
                        keyPairData.x509Certificate(), generatedTokens.accessDuration());
                tokenCacheWriterService.saveRefreshToken(projectId, generatedTokens.refreshClaims().jti(),
                        generatedTokens.accessClaims().jti(), generatedTokens.tokenResponse().getRefreshToken(),
                        keyPairData.x509Certificate(), generatedTokens.refreshDuration());
                return generatedTokens.tokenResponse();
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
            Optional<ClientDto> clientDtoOptional = clientService.get(projectId, clientCredentials.id());
            Optional<KeyPairData> keyPairDataOptional = clientService.getDefaultKeyPair(projectId, clientCredentials.id());
            if (clientDtoOptional.isPresent() && keyPairDataOptional.isPresent()) {
                LOGGER.info("Generating client tokens for {} {} {} {}", issuerUri, projectId, clientCredentials.id(), clientCredentials.id());
                ProjectDto project = projectDto.get();
                ClientDto client = clientDtoOptional.get();
                if (client.authEnabled()) {
                    Set<PermissionDto> permissions = permissionService.getPermissions(client.roles());
                    KeyPairData keyPairData = keyPairDataOptional.get();

                    TokenGenerator tokenGenerator = tokenGenerators.get(DEFAULT);
                    GeneratedTokens generatedTokens = tokenGenerator.generateClientTokens(issuerUri, project, client, keyPairData, permissions, requestedAudience, requestedScopes);

                    tokenCacheWriterService.saveAccessToken(projectId, generatedTokens.accessClaims().jti(),
                            generatedTokens.refreshClaims().jti(), generatedTokens.tokenResponse().getAccessToken(),
                            keyPairData.x509Certificate(), generatedTokens.accessDuration());
                    tokenCacheWriterService.saveRefreshToken(projectId, generatedTokens.refreshClaims().jti(),
                            generatedTokens.accessClaims().jti(), generatedTokens.tokenResponse().getRefreshToken(),
                            keyPairData.x509Certificate(), generatedTokens.refreshDuration());
                    return generatedTokens.tokenResponse();
                } else {
                    throw new OAuth2TokenException("Not Authorized or Not Found !");
                }
            } else {
                LOGGER.warn("Client not found: {}", clientCredentials.id());
                throw new OAuth2TokenException("User not found !");
            }
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
                Set<PermissionDto> permissions = permissionService.getPermissions(user.roles());

                TokenGenerator tokenGenerator = tokenGenerators.get(DEFAULT);
                GeneratedTokens generatedTokens = tokenGenerator.refreshTokens(project, user, keyPairData, permissions, refreshClaims, refreshToken);

                tokenCacheWriterService.saveRefreshedAccessToken(projectId, generatedTokens.accessClaims().jti(),
                        refreshClaims.jti(), generatedTokens.tokenResponse().getAccessToken(),
                        keyPairData.x509Certificate(), generatedTokens.accessDuration());
                return generatedTokens.tokenResponse();
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

}
