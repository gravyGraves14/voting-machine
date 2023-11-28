package edu.unm.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * created by:
 * author: MichaelMillar
 */
public class DateUtils {

    // STATIC USE ONLY
    private DateUtils() {}

    private static final SimpleDateFormat formatter =
            new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return formatter.format(dateTime);
    }

}
