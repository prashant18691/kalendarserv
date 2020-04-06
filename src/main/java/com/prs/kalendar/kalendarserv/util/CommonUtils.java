package com.prs.kalendar.kalendarserv.util;

import com.prs.kalendar.kalendarserv.exception.custom.InvalidDateException;
import com.prs.kalendar.kalendarserv.exception.custom.PastDateTimeException;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CommonUtils {

    //yyyy-MM-dd'T'HH:mm:ssZ
 /*Date date = new Date();
 DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
 String dateAsISOString = df.format(date);*/

    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm" ;

    public static final String EMAIL_REGEX = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9]"
            + "(?:[A-Za-z0-9-]*[A-Za-z0-9])?";

    public static final String DATE_REGEX = "^((0[1-9])|([12][0-9])|(3[01]))-((0[1-9])|1[012])-\\d\\d\\d\\d";

    public static Timestamp strToTimeStamp(String dateStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        try {
            return Timestamp.valueOf(LocalDateTime.from(formatter.parse(dateStr)));
        }
        catch (DateTimeParseException e){
            throw new InvalidDateException("Specify a valid date-time in the format dd-MM-yyyy HH:mm");
        }
    }

    public static String timeStampToStr(Timestamp date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return formatter.format(date.toLocalDateTime());
    }

    public static String getCurrentDateTime(){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        return formatter.format(new Date());
    }

    public static boolean checkPastDateTime(String slotDateTime) throws PastDateTimeException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp timestamp = CommonUtils.strToTimeStamp(slotDateTime);
        if (!timestamp.before(now) || timestamp.after(now))
            return false;

        throw new PastDateTimeException("Specify a date-time in present or future in the format dd-MM-yyyy HH:mm");
    }

    public static boolean checkPastDate(String date) throws PastDateTimeException {
        Date currDate = new Date();
        date=date+" 00:00";
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            final Date parseDate = formatter.parse(date);
            if(DateUtils.isSameDay(parseDate,currDate) || parseDate.after(currDate)){
                return true;
            }
            throw new PastDateTimeException("Specify a date in present or future in the format dd-MM-yyyy");
        } catch (ParseException e) {
            throw new InvalidDateException("Specify a valid date in the format dd-MM-yyyy");
        }
    }

    public static Timestamp getEndDateTime(Timestamp timestampOld) {
        ZonedDateTime zonedDateTime = timestampOld.toInstant().atZone(ZoneId.of("Asia/Kolkata"));
        return Timestamp.from(zonedDateTime.plus(1, ChronoUnit.HOURS).toInstant());
    }

    public static Date getTimeStampToDate(Timestamp timestamp){
        return new Date(timestamp.getTime());
    }

    public static Date getDateFromStr(String date) {
        date=date+" 00:00";
        DateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new InvalidDateException("Specify a valid date in the format dd-MM-yyyy");
        }
    }
}