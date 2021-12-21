package one.microproject.authx.common.dto;

import java.util.List;

public record AuthxDto(String id, List<String> globalAdminProjectIds) {
}
