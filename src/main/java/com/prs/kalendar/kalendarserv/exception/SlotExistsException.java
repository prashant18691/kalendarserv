package com.prs.kalendar.kalendarserv.exception;

public class SlotExistsException extends RuntimeException {
    public SlotExistsException(String message) {
        super(message);
    }
    public SlotExistsException(Throwable cause) {
        super(cause);
    }
    public SlotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
