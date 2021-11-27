package one.microproject.authx.common.tests;

import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.KeyPairSerialized;
import one.microproject.authx.common.utils.CryptoUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.Security;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CryptoUtilsTest {

    @BeforeAll
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    void testSha512Digest() {
        String digest = CryptoUtils.getSha512HashBase64("data");
        assertEquals("d8fOml2GuzhtRDu5Y5D6oSBjMVhpnIhEwwsTqwv5J2C35EFq6jl9uRtKwOXdVrjvfksGYWKrH9wIgxnObe/Idg==", digest);
    }

    @Test
    void testSerializeDeserializeKeypair() {
        KeyPairData keyPairData = CryptoUtils.generateSelfSignedKeyPair("kid-001", "iss", Instant.now(), TimeUnit.SECONDS, 10L);
        KeyPairSerialized keyPairSerialized = CryptoUtils.map(keyPairData);
        KeyPairData keyPairDataDeserialized = CryptoUtils.map(keyPairSerialized);
        assertEquals(keyPairData, keyPairDataDeserialized);
    }

    @Test
    void testGenerateKeyPair() {
        KeyPairData keyPairData = CryptoUtils.generateSelfSignedKeyPair("kid-001", "iss", Instant.now(), TimeUnit.SECONDS, 10L);
        assertNotNull(keyPairData);
        assertNotNull(keyPairData.privateKey());
        assertNotNull(keyPairData.x509Certificate());
        assertEquals("kid-001", keyPairData.id());
    }

}
