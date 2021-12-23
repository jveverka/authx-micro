package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record UserDto(@JsonProperty("id") String id,
                      @JsonProperty("clientId") String clientId,
                      @JsonProperty("email") String email,
                      @JsonProperty("description") String description,
                      @JsonProperty("secret") String secret,
                      @JsonProperty("labels") Map<String, String> labels) {
}
