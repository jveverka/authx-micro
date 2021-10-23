package one.microproject.authx.service.dto;

import java.util.Map;

public record ClientDto(String id, String projectId, String description, Boolean authEnabled, Map<String, String> labels) {
}
