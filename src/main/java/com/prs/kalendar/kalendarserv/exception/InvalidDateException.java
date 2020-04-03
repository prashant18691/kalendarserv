package com.prs.kalendar.kalendarserv.exception;


public class InvalidDateException extends RuntimeException {
    public InvalidDateException(String message) {
        super(message);
    }
    public InvalidDateException(Throwable cause) {
        super(cause);
    }
    public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }
}

