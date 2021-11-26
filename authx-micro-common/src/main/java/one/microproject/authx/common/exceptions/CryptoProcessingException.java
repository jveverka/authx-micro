package one.microproject.authx.common.exceptions;

public class CryptoProcessingException extends RuntimeException {

    public CryptoProcessingException() {
    }

    public CryptoProcessingException(String message) {
        super(message);
    }

    public CryptoProcessingException(Throwable cause) {
        super(cause);
    }

}
