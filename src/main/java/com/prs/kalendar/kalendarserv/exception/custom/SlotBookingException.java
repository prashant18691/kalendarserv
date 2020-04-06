package com.prs.kalendar.kalendarserv.exception.custom;

public class SlotBookingException extends RuntimeException {
    public SlotBookingException(String message) {
        super(message);
    }
    public SlotBookingException(Throwable cause) {
        super(cause);
    }
    public SlotBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}