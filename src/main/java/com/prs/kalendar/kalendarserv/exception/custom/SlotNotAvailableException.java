package com.prs.kalendar.kalendarserv.exception.custom;

public class SlotNotAvailableException extends RuntimeException {
    public SlotNotAvailableException(String message) {
        super(message);
    }
    public SlotNotAvailableException(Throwable cause) {
        super(cause);
    }
    public SlotNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
