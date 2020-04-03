package com.prs.kalendar.kalendarserv.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
    public UserExistsException(Throwable cause) {
        super(cause);
    }
    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
