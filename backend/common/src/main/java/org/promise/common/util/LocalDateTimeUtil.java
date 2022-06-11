package org.promise.common.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {

    private static final DateTimeFormatter df=DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime asLocalDateTime(String dateTimeString){
        return LocalDateTime.parse (dateTimeString,df);
    }

    public static String asString(LocalDateTime dateTime){
        return df.format (dateTime);
    }
}
