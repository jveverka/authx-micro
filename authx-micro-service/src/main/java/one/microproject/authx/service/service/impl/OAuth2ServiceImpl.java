package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.service.dto.IntrospectResponse;
import one.microproject.authx.service.dto.JWKResponse;
import one.microproject.authx.service.dto.ProviderConfigurationResponse;
import one.microproject.authx.service.dto.TokenResponse;
import one.microproject.authx.service.dto.UserInfoResponse;
import one.microproject.authx.service.service.ClientService;
import one.microproject.authx.service.service.OAuth2Service;
import one.microproject.authx.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.net.URI;
import java.util.Optional;
import java.util.Set;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    private final UserService userService;
    private final ClientService clientService;

    @Autowired
    public OAuth2ServiceImpl(UserService userService, ClientService clientService) {
        this.userService = userService;
        this.clientService = clientService;
    }

    @Override
    public TokenResponse getTokenForPassword(URI issuerUri, String projectId, ClientCredentials clientCredentials, Set<String> scopes, UserCredentials userCredentials) {
        Boolean clientOk = clientService.verifySecret(projectId, clientCredentials.id(), clientCredentials.secret());
        Boolean userOk = userService.verifySecret(projectId, userCredentials.username(), userCredentials.password());
        if (clientOk && userOk) {

        } else {

        }
        return null;
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
