package one.microproject.authx.common.dto;

import java.util.Map;

public record GroupDto(String id, String projectId, String description, Map<String, String> labels) {
}
