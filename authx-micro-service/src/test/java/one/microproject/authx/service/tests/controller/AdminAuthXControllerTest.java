package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

class AdminAuthXControllerTest extends AppBaseTest {

    @Test
    void testCreateAndDeleteProject() {
        TokenResponse globalAdminTokens = getGlobalAdminTokens();
        AuthXClient authXClient = getAuthXClient();
    }

}
