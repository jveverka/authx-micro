package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.service.dto.TokenResponse;

import java.util.Set;

public interface OAuth2Service {

    TokenResponse getTokenForPassword(String projectId, ClientCredentials clientCredentials, Set<String> scopes);

    TokenResponse getTokenForClientCredentials(String projectId, ClientCredentials clientCredentials, Set<String> scopes);

    TokenResponse getTokenForRefreshToken(String projectId, ClientCredentials clientCredentials, Set<String> scopes);

}
