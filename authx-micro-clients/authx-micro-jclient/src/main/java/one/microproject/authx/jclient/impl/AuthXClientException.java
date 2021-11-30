package one.microproject.authx.jclient.impl;

public class AuthXClientException extends RuntimeException {

    public AuthXClientException() {
    }

    public AuthXClientException(String message) {
        super(message);
    }

    public AuthXClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthXClientException(Throwable cause) {
        super(cause);
    }

}
