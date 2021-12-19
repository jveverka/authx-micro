package one.microproject.authx.common.dto;

import java.util.Map;

public record UpdateProjectRequest(String id, String description, Map<String, String> labels) {
}
