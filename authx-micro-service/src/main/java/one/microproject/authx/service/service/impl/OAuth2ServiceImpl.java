package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.service.dto.IntrospectResponse;
import one.microproject.authx.service.dto.JWKResponse;
import one.microproject.authx.service.dto.ProviderConfigurationResponse;
import one.microproject.authx.service.dto.TokenResponse;
import one.microproject.authx.service.dto.UserInfoResponse;
import one.microproject.authx.service.service.OAuth2Service;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    @Override
    public TokenResponse getTokenForPassword(URI issuerUri, String projectId, ClientCredentials clientCredentials, Set<String> scopes, UserCredentials userCredentials) {
        return null;
    }

    @Override
    public TokenResponse getTokenForClientCredentials(URI issuerUri, String projectId, ClientCredentials clientCredentials, Set<String> scopes) {
        return null;
    }

    @Override
    public TokenResponse getTokenForRefreshToken(URI issuerUri, String projectId, ClientCredentials clientCredentials, Set<String> scopes, String refreshToken) {
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
