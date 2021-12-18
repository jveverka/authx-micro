package one.microproject.authx.common.dto;

import java.util.Map;
import java.util.Set;

public record CreateClientRequest(String id, String description, Boolean authEnabled, String secret, Map<String, String> labels, Set<String> roles, Set<String> groups) {
}
