package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.AuthXResponse;
import one.microproject.authx.common.dto.AuthxInfo;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemControllerTest extends AppBaseTest {

    @Test
    void testAuthxInfo() {
        AuthXClient adminClient = getAuthXClient();
        AuthXResponse<AuthxInfo, Void> response = adminClient.getAuthxInfo();
        assertNotNull(response);
        assertTrue(response.isSuccess());

        AuthxInfo authxInfo = response.response();
        assertNotNull(authxInfo.id());
        assertNotNull(authxInfo.projects());
        assertEquals(1, authxInfo.projects().size());
    }

}
