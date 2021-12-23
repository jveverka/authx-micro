package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserCredentials(@JsonProperty("username") String username,
                              @JsonProperty("password") String password) {
}
