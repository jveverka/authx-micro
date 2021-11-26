package one.microproject.authx.common.tests;


import org.junit.jupiter.api.Test;

import static one.microproject.authx.common.utils.ServiceUtils.createId;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceUtilsTest {

    @Test
    void testCreateId() {
        String id = createId("project-id", "id");
        assertEquals("project-id-id", id);
    }

}
