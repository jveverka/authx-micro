package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public record CreateClientRequest(@JsonProperty("id") String id,
                                  @JsonProperty("description") String description,
                                  @JsonProperty("authEnabled") Boolean authEnabled,
                                  @JsonProperty("secret") String secret,
                                  @JsonProperty("labels") Map<String, String> labels,
                                  @JsonProperty("roles") Set<String> roles,
                                  @JsonProperty("groups") Set<String> groups) {
}
