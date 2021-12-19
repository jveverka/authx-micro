package one.microproject.authx.service.tests.service;

import one.microproject.authx.common.dto.AuthxDto;
import one.microproject.authx.common.dto.CreateProjectRequest;
import one.microproject.authx.service.model.Authx;
import one.microproject.authx.service.service.AuthXService;
import one.microproject.authx.service.service.DataInitService;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthXServiceTest extends AppBaseTest {

    @Autowired
    AuthxDto authxDto;

    @Autowired
    CreateProjectRequest createProjectRequest;

    @Autowired
    AuthXService authXService;

    @Autowired
    DataInitService dataInitService;

    @Test
    void testInitialDataModel() {
        Optional<Authx> authx = authXService.getAuthxInfo();
        assertTrue(authx.isPresent());
        assertEquals(authxDto.id(), authx.get().getId());
        assertTrue(authx.get().getGlobalAdminProjectIds().contains(createProjectRequest.id()));
    }

}
