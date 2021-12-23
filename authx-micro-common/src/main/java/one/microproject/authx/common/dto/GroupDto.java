package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record GroupDto(@JsonProperty("id") String id,
                       @JsonProperty("projectId") String projectId,
                       @JsonProperty("description") String description,
                       @JsonProperty("labels") Map<String, String> labels) {
}
