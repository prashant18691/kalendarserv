package com.prs.kalendar.kalendarserv.exception.custom;

public class GoogleAuthorizationException extends RuntimeException {
    public GoogleAuthorizationException(String message) {
        super(message);
    }
    public GoogleAuthorizationException(Throwable cause) {
        super(cause);
    }
    public GoogleAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
