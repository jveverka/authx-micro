package one.microproject.authx.common.dto;

import java.util.Map;

public record UserDto(String id, String clientId, String email, String description, String secret, Map<String, String> labels) {
}
