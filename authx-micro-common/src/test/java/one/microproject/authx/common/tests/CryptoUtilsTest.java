package one.microproject.authx.common.tests;

import org.junit.jupiter.api.Test;

import static one.microproject.authx.common.utils.CryptoUtils.getSha512HashBase64;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CryptoUtilsTest {

    @Test
    void testSha512Digest() {
        String digest = getSha512HashBase64("data");
        assertEquals("d8fOml2GuzhtRDu5Y5D6oSBjMVhpnIhEwwsTqwv5J2C35EFq6jl9uRtKwOXdVrjvfksGYWKrH9wIgxnObe/Idg==", digest);
    }

}
