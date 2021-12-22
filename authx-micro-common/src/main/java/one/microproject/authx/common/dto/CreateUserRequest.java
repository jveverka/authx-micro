package one.microproject.authx.common.dto;

import java.util.Map;
import java.util.Set;

public record CreateUserRequest(String id, String email, String description, String secret, Map<String, String> labels, Set<String> roles, Set<String> groups, String clientId) {
}
