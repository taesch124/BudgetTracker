package com.projects.danieltaeschler.budgettracker.utilities;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel Taeschler on 12/11/2015.
 */
public class DateFormatter extends SimpleDateFormat{
    private static final String TAG = "DateFormatter";

    private static SimpleDateFormat longDateFormatter = new SimpleDateFormat("MMMM d, yyyy");
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm a MM/dd/yyyy");

    public static String returnSimpleDate(Date date) {
        String dateString = dateFormatter.format(date);

        return dateString;
    }

    public static String returnLongDate(Date date) {
        String dateString = longDateFormatter.format(date);

        return dateString;
    }

    public static Date parseDateFromString(Date prevDate, String string) {
        Date parsedDate = null;
        Log.d(TAG, "String to parse: " + string);
        try {
            parsedDate = dateFormatter.parse(string);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            parsedDate = calendar.getTime();
        } catch (Exception e) {
            parsedDate = prevDate;
            Log.e(TAG, "Error parsing date");

        }

        return parsedDate;
    }

    public static Date returnFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date newDate = cal.getTime();
        return newDate;
    }

    public static Date returnLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        Date newDate = cal.getTime();
        return newDate;
    }

    public static Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static int returnIntLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return day;
    }

    public static String returnSimpleDateTime(Date date) {
        String dateTimeString = dateTimeFormatter.format(date);

        return dateTimeString;
    }


}
