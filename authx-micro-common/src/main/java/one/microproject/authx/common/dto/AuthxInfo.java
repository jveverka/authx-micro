package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AuthxInfo(@JsonProperty("id") String id,
                        @JsonProperty("projects") List<String> projects) {
}
