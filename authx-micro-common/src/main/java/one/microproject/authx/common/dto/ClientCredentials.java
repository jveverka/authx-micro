package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record ClientCredentials(@JsonProperty("id") String id,
                                @JsonProperty("secret") String secret) {
}
