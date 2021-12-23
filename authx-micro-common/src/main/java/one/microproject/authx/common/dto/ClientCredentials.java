package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClientCredentials(@JsonProperty("id") String id,
                                @JsonProperty("secret") String secret) {
}
