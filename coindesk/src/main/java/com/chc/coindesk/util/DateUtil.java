package com.chc.coindesk.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {
    public static String updateukDateToLocalDateTime(String inputDate, String inputFormat) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat, Locale.UK);

        LocalDateTime inputDateTime = LocalDateTime.parse(inputDate, inputFormatter);
        DateTimeFormatter outPutFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return inputDateTime.format(outPutFormatter);

    }
}
