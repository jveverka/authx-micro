package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OAuth2ControllerTest extends AppBaseTest  {

    @Test
    void testGetTokenForPassword() {
        AuthXClient authXClient = getGlobalAdminClient();
        ClientCredentials clientCredentials = new ClientCredentials("admin-client", "secret");
        Set<String> scopes = Set.of();
        UserCredentials userCredentials = new UserCredentials("admin-user", "s3cr3t");
        TokenResponse tokenForPassword = authXClient.getTokenForPassword(clientCredentials, "", scopes, userCredentials);
        assertNotNull(tokenForPassword);
        assertNotNull(tokenForPassword.getAccessToken());
        assertNotNull(tokenForPassword.getRefreshToken());

        IntrospectResponse introspectResponse = authXClient.introspect(tokenForPassword.getAccessToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.getActive());

        introspectResponse = authXClient.introspect(tokenForPassword.getRefreshToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.getActive());

        authXClient.revoke(tokenForPassword.getAccessToken(), "");
        authXClient.revoke(tokenForPassword.getRefreshToken(), "");

        introspectResponse = authXClient.introspect(tokenForPassword.getAccessToken(), "");
        assertNotNull(introspectResponse);
        assertFalse(introspectResponse.getActive());

        introspectResponse = authXClient.introspect(tokenForPassword.getRefreshToken(), "");
        assertNotNull(introspectResponse);
        assertFalse(introspectResponse.getActive());
    }

}
