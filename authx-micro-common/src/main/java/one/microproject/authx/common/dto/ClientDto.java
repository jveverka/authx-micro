package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public record ClientDto(@JsonProperty("id") String id,
                        @JsonProperty("description") String description,
                        @JsonProperty("labels") Map<String, String> labels,
                        @JsonProperty("roles") Set<String> roles,
                        @JsonProperty("groups") Set<String> groups) {
}
