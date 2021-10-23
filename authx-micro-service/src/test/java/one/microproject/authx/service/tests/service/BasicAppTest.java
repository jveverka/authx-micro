package one.microproject.authx.service.tests.service;

import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static com.mongodb.assertions.Assertions.assertNotNull;

class BasicAppTest extends AppBaseTest {

    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
    }

}
