package one.microproject.authx.common.exceptions;

public class JWTProcessingException extends RuntimeException {

    public JWTProcessingException() {
    }

    public JWTProcessingException(String message) {
        super(message);
    }

    public JWTProcessingException(Throwable cause) {
        super(cause);
    }

}
