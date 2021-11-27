package one.microproject.authx.service.exceptions;

public class OAuth2TokenException extends RuntimeException {

    public OAuth2TokenException() {
    }

    public OAuth2TokenException(String message) {
        super(message);
    }

    public OAuth2TokenException(Throwable cause) {
        super(cause);
    }

}
