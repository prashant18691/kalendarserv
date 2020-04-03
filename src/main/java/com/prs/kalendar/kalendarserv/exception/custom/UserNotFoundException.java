package com.prs.kalendar.kalendarserv.exception.custom;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
