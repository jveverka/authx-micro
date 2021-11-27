package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.service.dto.TokenResponse;
import one.microproject.authx.service.service.OAuth2Service;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    @Override
    public TokenResponse getTokenForPassword(String projectId, ClientCredentials clientCredentials, Set<String> scopes) {
        return null;
    }

    @Override
    public TokenResponse getTokenForClientCredentials(String projectId, ClientCredentials clientCredentials, Set<String> scopes) {
        return null;
    }

    @Override
    public TokenResponse getTokenForRefreshToken(String projectId, ClientCredentials clientCredentials, Set<String> scopes) {
        return null;
    }

}
