package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AuthxDto(@JsonProperty("id") String id,
                       @JsonProperty("globalAdminProjectIds") List<String> globalAdminProjectIds) {
}
