package com.prs.kalendar.kalendarserv.validators;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PresentOrFutureValidator
        implements ConstraintValidator<PresentOrFuture, String> {

    public final void initialize(final PresentOrFuture annotation) {}

    public final boolean isValid(final String value,
                                 final ConstraintValidatorContext context) {
/*
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date today = calendar.getTime();*/

    if (StringUtils.isEmpty(value))
        return false;

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(value);

        return !dateTime.isBefore(now) || dateTime.isAfter(now);

    }
}
