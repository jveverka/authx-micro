package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXOAuth2Client;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OAuth2ControllerTest extends AppBaseTest  {

    @Test
    void testGetTokenForPassword() {
        AuthXOAuth2Client authXClient = getGlobalAdminOAuth2Client();
        ClientCredentials clientCredentials = new ClientCredentials("admin-client", "secret");
        Set<String> scopes = Set.of();
        UserCredentials userCredentials = new UserCredentials("admin-user", "s3cr3t");
        TokenResponse tokensForPassword = authXClient.getTokenForPassword(clientCredentials, "", scopes, userCredentials);
        assertNotNull(tokensForPassword);
        assertNotNull(tokensForPassword.getAccessToken());
        assertNotNull(tokensForPassword.getRefreshToken());

        IntrospectResponse introspectResponse = authXClient.introspect(tokensForPassword.getAccessToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.getActive());

        introspectResponse = authXClient.introspect(tokensForPassword.getRefreshToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.getActive());

        authXClient.revoke(tokensForPassword.getAccessToken(), "");
        authXClient.revoke(tokensForPassword.getRefreshToken(), "");

        introspectResponse = authXClient.introspect(tokensForPassword.getAccessToken(), "");
        assertNotNull(introspectResponse);
        assertFalse(introspectResponse.getActive());

        introspectResponse = authXClient.introspect(tokensForPassword.getRefreshToken(), "");
        assertNotNull(introspectResponse);
        assertFalse(introspectResponse.getActive());
    }

    @Test
    void testRefreshToken() {
        AuthXOAuth2Client authXClient = getGlobalAdminOAuth2Client();
        ClientCredentials clientCredentials = new ClientCredentials("admin-client", "secret");
        Set<String> scopes = Set.of();
        UserCredentials userCredentials = new UserCredentials("admin-user", "s3cr3t");
        TokenResponse tokensForPassword = authXClient.getTokenForPassword(clientCredentials, "", scopes, userCredentials);

        IntrospectResponse introspectResponse = authXClient.introspect(tokensForPassword.getAccessToken(), "");
        assertTrue(introspectResponse.getActive());
        introspectResponse = authXClient.introspect(tokensForPassword.getRefreshToken(), "");
        assertTrue(introspectResponse.getActive());

        TokenResponse tokensForRefresh = authXClient.refreshToken(clientCredentials, tokensForPassword.getRefreshToken());

        introspectResponse = authXClient.introspect(tokensForPassword.getAccessToken(), "");
        assertFalse(introspectResponse.getActive());
        introspectResponse = authXClient.introspect(tokensForPassword.getRefreshToken(), "");
        assertTrue(introspectResponse.getActive());
        introspectResponse = authXClient.introspect(tokensForRefresh.getAccessToken(), "");
        assertTrue(introspectResponse.getActive());
        introspectResponse = authXClient.introspect(tokensForRefresh.getRefreshToken(), "");
        assertTrue(introspectResponse.getActive());
    }

}
