package one.microproject.authx.jclient.impl;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;

import java.util.Set;

public class AuthXClientImpl implements AuthXClient {

    private final String baseUrl;
    private final String projectId;

    public AuthXClientImpl(String baseUrl, String projectId) {
        this.baseUrl = baseUrl;
        this.projectId = projectId;
    }


    @Override
    public TokenResponse getTokenForPassword(ClientCredentials clientCredentials, Set<String> scopes, UserCredentials userCredentials) {
        return null;
    }

}
