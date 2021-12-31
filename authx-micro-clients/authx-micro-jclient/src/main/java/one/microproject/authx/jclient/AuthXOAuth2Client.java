package one.microproject.authx.jclient;

import one.microproject.authx.common.dto.AuthXResponse;
import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.Empty;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.*;

import java.util.Set;

public interface AuthXOAuth2Client {

    AuthXResponse<TokenResponse, Void> getTokenForClient(ClientCredentials clientCredentials, String audience, Set<String> scopes);

    AuthXResponse<TokenResponse, Void> getTokenForPassword(ClientCredentials clientCredentials, String audience, Set<String> scopes, UserCredentials userCredentials);

    AuthXResponse<IntrospectResponse, Void> introspect(String token, String typeHint);

    AuthXResponse<Empty, Void> revoke(String token, String typeHint);

    AuthXResponse<TokenResponse, Void> refreshToken(ClientCredentials clientCredentials, String refreshToken);

    AuthXResponse<ProviderConfigurationResponse, Void> getConfiguration();

    AuthXResponse<JWKResponse, Void> getCerts();

    AuthXResponse<UserInfoResponse, Void> getUserInfo(String token);

}
