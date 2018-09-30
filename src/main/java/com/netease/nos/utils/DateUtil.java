package com.netease.nos.utils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by future on 2018/9/30
 */
public class DateUtil {

    private static final DateTimeFormatter RFCDateFormate = DateTimeFormat
            .forPattern("EEE, dd MMM yyyy HH:mm:ss ZZZ").withLocale(Locale.US);

    public static String getRFCDateFromLong(long date) {
        try {
            return RFCDateFormate.print(date);
        } catch (Exception e) {
            return null;
        }

    }

}
