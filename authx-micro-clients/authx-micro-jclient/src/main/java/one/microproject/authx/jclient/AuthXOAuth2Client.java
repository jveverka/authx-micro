package one.microproject.authx.jclient;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.JWKResponse;
import one.microproject.authx.common.dto.oauth2.ProviderConfigurationResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.common.dto.oauth2.UserInfoResponse;

import java.util.Set;

public interface AuthXOAuth2Client {

    TokenResponse getTokenForClient(ClientCredentials clientCredentials, String audience, Set<String> scopes);

    TokenResponse getTokenForPassword(ClientCredentials clientCredentials, String audience, Set<String> scopes, UserCredentials userCredentials);

    IntrospectResponse introspect(String token, String typeHint);

    void revoke(String token, String typeHint);

    TokenResponse refreshToken(ClientCredentials clientCredentials, String refreshToken);

    ProviderConfigurationResponse getConfiguration();

    JWKResponse getCerts();

    UserInfoResponse getUserInfo(String token);

}
