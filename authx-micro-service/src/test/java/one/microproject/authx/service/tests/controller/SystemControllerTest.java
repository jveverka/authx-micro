package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.AuthxInfo;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SystemControllerTest extends AppBaseTest {

    @Test
    void testAuthxInfo() {
        AuthXClient adminClient = getAuthXClient();
        AuthxInfo authxInfo = adminClient.getAuthxInfo();
        assertNotNull(authxInfo);
        assertNotNull(authxInfo.id());
        assertNotNull(authxInfo.projects());
        assertEquals(1, authxInfo.projects().size());
    }

}
