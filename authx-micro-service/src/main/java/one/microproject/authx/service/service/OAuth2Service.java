package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.JWKResponse;
import one.microproject.authx.common.dto.oauth2.ProviderConfigurationResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.common.dto.oauth2.UserInfoResponse;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

public interface OAuth2Service {

    TokenResponse getTokenForPassword(URI issuerUri, String projectId, ClientCredentials clientCredentials, Set<String> scopes, UserCredentials userCredentials);

    TokenResponse getTokenForClientCredentials(URI issuerUri, String projectId, ClientCredentials clientCredentials, Set<String> scopes);

    TokenResponse getTokenForRefreshToken(URI issuerUri, String projectId, ClientCredentials clientCredentials, Set<String> scopes, String refreshToken);

    ProviderConfigurationResponse getProviderConfiguration(URI issuerUri, String projectId);

    JWKResponse getJWKResponse(String projectId);

    IntrospectResponse getIntrospectResponse(String projectId, String token, String tokenTypeHint);

    void revoke(String projectId, String token, String tokenTypeHint);

    Optional<UserInfoResponse> getUserInfo(String projectId, String token);

}
