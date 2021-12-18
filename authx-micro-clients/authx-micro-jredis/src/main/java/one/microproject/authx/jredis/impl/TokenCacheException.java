package one.microproject.authx.jredis.impl;

public class TokenCacheException extends RuntimeException {

    public TokenCacheException() {
    }

    public TokenCacheException(String message) {
        super(message);
    }

    public TokenCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenCacheException(Throwable cause) {
        super(cause);
    }

}
