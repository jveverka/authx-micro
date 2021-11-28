package one.microproject.authx.jclient;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.TokenResponse;

import java.util.Set;

public interface AuthXClient {

    TokenResponse getTokenForPassword(ClientCredentials clientCredentials, Set<String> scopes, UserCredentials userCredentials);

}
