package one.microproject.authx.common.dto;

import java.util.List;

public record AuthxInfo(String id, List<String> projects) {
}
