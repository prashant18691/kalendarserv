package com.prs.kalendar.kalendarserv.util;

import com.prs.kalendar.kalendarserv.exception.InvalidDateException;
import com.prs.kalendar.kalendarserv.exception.PastDateTimeException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class CommonUtils {


    private static final String PATTERN = "dd/MM/yyyy HH:mm" ;

    public static Timestamp strToTimeStamp(String dateStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        try {
            return Timestamp.valueOf(LocalDateTime.from(formatter.parse(dateStr)));
        }
        catch (DateTimeParseException e){
            throw new InvalidDateException("Specify a valid date-time");
        }
    }

    public static String timeStampToStr(Timestamp date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return formatter.format(date.toLocalDateTime());
    }

    public static String getCurrentDateTime(){
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN);
        return formatter.format(new Date());
    }

    public static boolean checkPastDate(String slotDateTime) throws PastDateTimeException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp timestamp = CommonUtils.strToTimeStamp(slotDateTime);
        if (!timestamp.before(now) || timestamp.after(now))
            return false;

        throw new PastDateTimeException("Specify a valid date-time.");
    }
}
