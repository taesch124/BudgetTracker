package com.projects.danieltaeschler.budgettracker.utilities;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Daniel Taeschler on 12/11/2015.
 */
public class DateFormatter extends SimpleDateFormat{
    private static final String TAG = "DateFormatter";

    private static SimpleDateFormat longDateFormatter = new SimpleDateFormat("MMMM d, yyyy");
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm MM/dd/yyyy");

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
        } catch (Exception e) {
            parsedDate = prevDate;
            Log.e(TAG, "Error parsing date");

        }

        return parsedDate;
    }

    public static String returnSimpleDateTime(Date date) {
        String dateTimeString = dateTimeFormatter.format(date);

        return dateTimeString;
    }


}
