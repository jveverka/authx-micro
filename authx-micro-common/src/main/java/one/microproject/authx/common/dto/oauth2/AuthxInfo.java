package one.microproject.authx.common.dto.oauth2;

import java.util.List;

public record AuthxInfo(String id, List<String> projects) {
}
