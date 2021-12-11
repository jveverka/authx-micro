package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.KeyPairSerialized;
import one.microproject.authx.common.dto.ProjectDto;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.UserDto;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.JWKResponse;
import one.microproject.authx.common.dto.oauth2.ProviderConfigurationResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.common.dto.oauth2.UserInfoResponse;
import one.microproject.authx.common.utils.LabelUtils;
import one.microproject.authx.common.utils.TokenUtils;
import one.microproject.authx.service.exceptions.OAuth2TokenException;
import one.microproject.authx.service.service.ClientService;
import one.microproject.authx.service.service.OAuth2Service;
import one.microproject.authx.service.service.ProjectService;
import one.microproject.authx.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    private final Long DEFAULT_ACCESS_DURATION = 24*60*60*1000L;
    private final Long DEFAULT_REFRESH_DURATION = 30*24*60*60*1000L;

    private final ProjectService projectService;
    private final UserService userService;
    private final ClientService clientService;

    @Autowired
    public OAuth2ServiceImpl(ProjectService projectService, UserService userService, ClientService clientService) {
        this.projectService = projectService;
        this.userService = userService;
        this.clientService = clientService;
    }

    @Override
    public TokenResponse getTokenForPassword(URI issuerUri, String projectId, ClientCredentials clientCredentials, Set<String> scopes, UserCredentials userCredentials) {
        Optional<ProjectDto> projectDto = projectService.get(projectId);
        if (projectDto.isEmpty()) {
            throw new OAuth2TokenException("Project not found !");
        }
        Boolean clientOk = clientService.verifySecret(projectId, clientCredentials.id(), clientCredentials.secret());
        Boolean userOk = userService.verifySecret(projectId, userCredentials.username(), userCredentials.password());
        if (clientOk && userOk) {
            Optional<UserDto> userDto = userService.get(projectId, userCredentials.username());
            Optional<KeyPairData> keyPairDataOptional = userService.getDefaultKeyPair(projectId, userCredentials.username());
            if (userDto.isPresent() && keyPairDataOptional.isPresent()) {
                ProjectDto project = projectDto.get();
                UserDto user = userDto.get();
                KeyPairData keyPairData = keyPairDataOptional.get();
                TokenClaims accessClaims = null; //new TokenClaims();
                TokenClaims refreshClaims = null; //new TokenClaims();
                TokenClaims idClaims = null; //new TokenClaims();
                String accessToken = TokenUtils.issueToken(accessClaims, keyPairData.id(), keyPairData.privateKey());
                String refreshToken = TokenUtils.issueToken(refreshClaims, keyPairData.id(), keyPairData.privateKey());
                String idToken = TokenUtils.issueToken(idClaims, keyPairData.id(), keyPairData.privateKey());
                Long expiresIn = LabelUtils.getAccessTokenDuration(DEFAULT_ACCESS_DURATION, project.labels(), user.labels());
                Long refreshExpiresIn = LabelUtils.getRefreshTokenDuration(DEFAULT_REFRESH_DURATION, project.labels(), user.labels());
                String tokenType = "Bearer";
                return new TokenResponse(accessToken, expiresIn, refreshExpiresIn, refreshToken, tokenType, idToken);
            } else {
                throw new OAuth2TokenException("User not found !");
            }
        } else {
            throw new OAuth2TokenException("Not Authorized or Not Found !");
        }
    }

    @Override
    public TokenResponse getTokenForClientCredentials(URI issuerUri, String projectId, ClientCredentials clientCredentials, Set<String> scopes) {
        Boolean clientOk = clientService.verifySecret(projectId, clientCredentials.id(), clientCredentials.secret());
        return null;
    }

    @Override
    public TokenResponse getTokenForRefreshToken(URI issuerUri, String projectId, ClientCredentials clientCredentials, Set<String> scopes, String refreshToken) {
        Boolean clientOk = clientService.verifySecret(projectId, clientCredentials.id(), clientCredentials.secret());
        return null;
    }

    @Override
    public ProviderConfigurationResponse getProviderConfiguration(URI issuerUri, String projectId) {
        return null;
    }

    @Override
    public JWKResponse getJWKResponse(String projectId) {
        return null;
    }

    @Override
    public IntrospectResponse getIntrospectResponse(String projectId, String token, String tokenTypeHint) {
        return null;
    }

    @Override
    public void revoke(String projectId, String token, String tokenTypeHint) {

    }

    @Override
    public Optional<UserInfoResponse> getUserInfo(String projectId, String token) {
        return Optional.empty();
    }

}
