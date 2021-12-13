package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    }

}