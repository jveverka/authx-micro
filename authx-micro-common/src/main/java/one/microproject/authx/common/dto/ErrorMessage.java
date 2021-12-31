package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorMessage(@JsonProperty("code") String code,
                           @JsonProperty("message") String message) {

    public static final String ERROR = "ERROR";

    public static ErrorMessage error(String message) {
        return new ErrorMessage(ERROR, message);
    }

    public static ErrorMessage error(String code, String message) {
        return new ErrorMessage(code, message);
    }

}
