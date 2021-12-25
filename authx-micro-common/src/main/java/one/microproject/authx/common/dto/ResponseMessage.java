package one.microproject.authx.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseMessage(@JsonProperty("success") boolean success,
                              @JsonProperty("code") String code,
                              @JsonProperty("message") String message) {

    public static final String OK = "OK";
    public static final String ERROR = "ERROR";

    public static ResponseMessage ok(String message) {
        return new ResponseMessage(true, OK, message);
    }

    public static ResponseMessage error(String message) {
        return new ResponseMessage(false, ERROR, message);
    }

    public static ResponseMessage of(String code, String message) {
        return new ResponseMessage(false, code, message);
    }

}
