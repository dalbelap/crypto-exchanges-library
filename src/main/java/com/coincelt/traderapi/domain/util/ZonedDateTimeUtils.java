package com.coincelt.traderapi.domain.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * LocalDateTime utils
 */
public class ZonedDateTimeUtils {

    private static final java.lang.String DATE_TIME_PATERN = "yyyy-MM-dd HH:mm:ss";

    public static ZonedDateTime stringToZonedDateTimeUTC(String dateAsString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATERN, Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(dateAsString, formatter);
        return localDateTime.atZone(ZoneOffset.UTC);
    }

    public static ZonedDateTime timestampToZonedDateTime(Long timestamp){
        return Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.UTC);
    }

    public static ZonedDateTime timestampAsDoubleToZonedDateTime(double timestamp) {
        long timestampAsInt = Math.round(timestamp * 1000);
        return Instant.ofEpochMilli(timestampAsInt).atZone(ZoneOffset.UTC);
    }
}
