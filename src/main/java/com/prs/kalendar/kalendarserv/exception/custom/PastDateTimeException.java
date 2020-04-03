package com.prs.kalendar.kalendarserv.exception.custom;

public class PastDateTimeException extends RuntimeException {
    public PastDateTimeException(String message) {
        super(message);
    }
    public PastDateTimeException(Throwable cause) {
        super(cause);
    }
    public PastDateTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
