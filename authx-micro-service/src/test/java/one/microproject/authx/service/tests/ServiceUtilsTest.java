package one.microproject.authx.service.tests;


import org.junit.jupiter.api.Test;

import static one.microproject.authx.service.service.impl.ServiceUtils.createId;
import static one.microproject.authx.service.service.impl.ServiceUtils.getSha512HashBase64;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceUtilsTest {

    @Test
    void testCreateId() {
        String id = createId("project-id", "id");
        assertEquals("project-id-id", id);
    }

    @Test
    void testSha512Digest() {
        String digest = getSha512HashBase64("data");
        assertEquals("d8fOml2GuzhtRDu5Y5D6oSBjMVhpnIhEwwsTqwv5J2C35EFq6jl9uRtKwOXdVrjvfksGYWKrH9wIgxnObe/Idg==", digest);
    }

}
