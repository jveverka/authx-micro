package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;
import java.util.Set;

public record CreateUserRequest(@JsonProperty("id") String id,
                                @JsonProperty("email") String email,
                                @JsonProperty("description") String description,
                                @JsonProperty("secret") String secret,
                                @JsonProperty("labels") Map<String, String> labels,
                                @JsonProperty("roles") Set<String> roles,
                                @JsonProperty("groups") Set<String> groups,
                                @JsonProperty("clientId") String clientId) {
}
