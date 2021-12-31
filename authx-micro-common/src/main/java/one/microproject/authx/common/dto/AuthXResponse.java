package one.microproject.authx.common.dto;

import java.util.Optional;

public record AuthXResponse<R, E>(R response, E error, int status) {

    public static <R, E> AuthXResponse<R, E> error(int status) {
        return new AuthXResponse<>(null, null, status);
    }

    public static <R, E> AuthXResponse<R, E> error(E error, int status) {
        return new AuthXResponse<>(null, error, status);
    }

    public static <R, E> AuthXResponse<R, E> ok(R response, int status) {
        return new AuthXResponse<>(response, null, status);
    }

    public static <E> AuthXResponse<Empty, E> ok(int status) {
        return new AuthXResponse<>(new Empty(), null, status);
    }

    public static AuthXResponse<String, ErrorMessage> ok(ResponseMessage responseMessage, int status) {
        return new AuthXResponse<>(responseMessage.message(), null, status);
    }

    public static AuthXResponse<String, ErrorMessage> error(ResponseMessage responseMessage, int status) {
        return new AuthXResponse<>(null, new ErrorMessage(responseMessage.code(), responseMessage.message()), status);
    }

    public boolean isSuccess() {
        return response != null;
    }

    public Optional<R> getResponse() {
        return Optional.ofNullable(response);
    }

    public Optional<E> getError() {
        return Optional.ofNullable(error);
    }

}
