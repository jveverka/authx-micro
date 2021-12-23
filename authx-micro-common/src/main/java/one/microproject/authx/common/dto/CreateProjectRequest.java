package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record CreateProjectRequest(@JsonProperty("id") String id,
                                   @JsonProperty("description") String description,
                                   @JsonProperty("labels") Map<String, String> labels,
                                   @JsonProperty("adminUser") CreateUserRequest adminUser,
                                   @JsonProperty("adminClient") CreateClientRequest adminClient) {
}
