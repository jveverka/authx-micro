package one.microproject.authx.common.dto;

public record KeyPairSerialized(String id, String x509Certificate, String privateKey) {
}
