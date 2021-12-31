package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.AuthXResponse;
import one.microproject.authx.common.dto.AuthXInfo;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemControllerTest extends AppBaseTest {

    @Test
    void testAuthxInfo() {
        AuthXClient adminClient = getAuthXClient();
        AuthXResponse<AuthXInfo, Void> response = adminClient.getAuthXInfo();
        assertNotNull(response);
        assertTrue(response.isSuccess());

        AuthXInfo authxInfo = response.response();
        assertNotNull(authxInfo.id());
        assertNotNull(authxInfo.projects());
        assertEquals(1, authxInfo.projects().size());
    }

}
