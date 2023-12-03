package edu.unm.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * created by:
 * author: MichaelMillar
 */
public class DateUtils {

    // STATIC USE ONLY
    private DateUtils() {}

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MM-dd-yyyy-HH-mm-ss");

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return formatter.format(dateTime);
    }

}
