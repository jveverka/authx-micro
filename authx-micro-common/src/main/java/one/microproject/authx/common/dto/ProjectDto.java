package one.microproject.authx.common.dto;

import java.util.Map;

public record ProjectDto(String id, String description, Map<String, String> labels) {
}
