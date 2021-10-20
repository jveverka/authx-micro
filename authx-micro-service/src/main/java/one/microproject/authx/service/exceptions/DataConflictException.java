package one.microproject.authx.service.exceptions;

public class DataConflictException extends RuntimeException {

    public DataConflictException() {
    }

    public DataConflictException(String message) {
        super(message);
    }

    public DataConflictException(Throwable cause) {
        super(cause);
    }

}
