package one.microproject.authx.service.dto;

import java.util.Map;

public record CreateProjectRequest(String id, String description, Map<String, String> labels, CreateUserRequest adminUser, CreateClientRequest adminClient) {
}
