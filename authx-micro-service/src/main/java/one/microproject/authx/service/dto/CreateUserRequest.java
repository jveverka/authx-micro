package one.microproject.authx.service.dto;

import java.util.Map;

public record CreateUserRequest(String id, String email, String description, String secret, Map<String, String> labels) {
}
