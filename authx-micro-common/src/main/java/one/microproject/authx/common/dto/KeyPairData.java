package one.microproject.authx.common.dto;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public record KeyPairData(String id, X509Certificate x509Certificate, PrivateKey privateKey) {
}
