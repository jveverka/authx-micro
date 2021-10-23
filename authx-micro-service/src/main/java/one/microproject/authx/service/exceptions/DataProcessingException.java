package one.microproject.authx.service.exceptions;

public class DataProcessingException extends RuntimeException {

    public DataProcessingException() {
    }

    public DataProcessingException(String message) {
        super(message);
    }

    public DataProcessingException(Throwable cause) {
        super(cause);
    }

}
