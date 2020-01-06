package com.github.enyata.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter{

    private static ThreadLocal<SimpleDateFormat> inDateFormatHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

    private static ThreadLocal<SimpleDateFormat> dateTimeFormatHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static String formatDate(Date date) {
        inDateFormatHolder.get().setLenient(false);
        return inDateFormatHolder.get().format(date);
    }

    public static String getCurrentTimeString() {
        dateTimeFormatHolder.get().setLenient(false);
        return dateTimeFormatHolder.get().format(new Date());
    }
}