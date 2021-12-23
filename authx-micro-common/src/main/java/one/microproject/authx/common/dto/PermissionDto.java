package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PermissionDto(@JsonProperty("id") String id,
                            @JsonProperty("description") String description,
                            @JsonProperty("service") String service,
                            @JsonProperty("resource") String resource,
                            @JsonProperty("action") String action) {
}
