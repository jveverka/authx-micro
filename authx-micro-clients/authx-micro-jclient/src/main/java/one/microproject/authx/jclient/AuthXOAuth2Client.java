package one.microproject.authx.jclient;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;

import java.util.Set;

public interface AuthXOAuth2Client {

    TokenResponse getTokenForPassword(ClientCredentials clientCredentials, String audience, Set<String> scopes, UserCredentials userCredentials);

    IntrospectResponse introspect(String token, String typeHint);

    void revoke(String token, String typeHint);

    TokenResponse refreshToken(ClientCredentials clientCredentials, String refreshToken);

}
