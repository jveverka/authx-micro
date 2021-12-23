package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeyPairSerialized(@JsonProperty("id") String id,
                                @JsonProperty("x509Certificate") String x509Certificate,
                                @JsonProperty("privateKey") String privateKey) {
}
