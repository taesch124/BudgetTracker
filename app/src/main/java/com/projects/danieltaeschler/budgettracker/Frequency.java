package com.projects.danieltaeschler.budgettracker;

import android.util.Log;

import com.projects.danieltaeschler.budgettracker.utilities.DateFormatter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel Taeschler on 5/17/2016.
 */
public enum Frequency {
    NONE(0),
    ANNUALLY(1),
    SEMI_ANNUALLY(2),
    QUARTERLY(4),
    BIMONTHLY(6),
    MONTHLY(12),
    BIWEEKLY(26),
    SEMI_MONTHLY(24),
    WEEKLY(52);

    private final static String TAG = "Frequency";

    private final int yearlyFrequency;

    Frequency (int frequency) {
        this.yearlyFrequency = frequency;
    }


    public int getYearlyFrequency() { return this.yearlyFrequency; }

    public static ArrayList<Frequency> getFrequenciesArrayList() {
        ArrayList<Frequency> frequencies = new ArrayList<>();

        for (Frequency f : Frequency.values() ) {
            frequencies.add(f);
        }

        return frequencies;
    }

    public static Frequency valueOf(int value) {
        Frequency[] frequencies = Frequency.values();

        for (Frequency f : frequencies) {
            if (f.getYearlyFrequency() == value) {
                return f;
            }
        }

        return Frequency.NONE;
    }

    public static Date addFrequencyToDate(Frequency frequency, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Log.d(TAG, "Adding frequency " + frequency.yearlyFrequency + " to " + DateFormatter.returnSimpleDate(date));

        switch (frequency.yearlyFrequency ) {
            case 0:
                //Do nothing - one time payment
                break;
            case 1:
                cal.add(Calendar.YEAR, 1);
                break;
            case 2:
                cal.add(Calendar.MONTH, 6);
                break;
            case 4:
                cal.add(Calendar.MONTH, 3);
                break;
            case 6:
                cal.add(Calendar.MONTH, 2);
                break;
            case 12:
                cal.add(Calendar.MONTH, 1);
                break;
            case 26:
                cal.add(Calendar.DAY_OF_YEAR, 14);
                break;
            case 24:
                //ToDo implement better logic for semi-monthly
                cal.add(Calendar.DAY_OF_YEAR, 14);
                break;
            case 52:
                cal.add(Calendar.DAY_OF_YEAR, 7);
                break;
            default:
                Log.d(TAG, "Bad value");
                break;
        }

        Date newDate = cal.getTime();

        return newDate;
    }

    public static Date subtractFrequencyFromDate(Frequency frequency, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Log.d(TAG, "Subtracting frequency " + frequency.yearlyFrequency + " to " + DateFormatter.returnSimpleDate(date));

        switch (frequency.yearlyFrequency ) {
            case 0:
                //Do nothing - one time payment
                break;
            case 1:
                cal.add(Calendar.YEAR, -1);
                break;
            case 2:
                cal.add(Calendar.MONTH, -6);
                break;
            case 4:
                cal.add(Calendar.MONTH, -3);
                break;
            case 6:
                cal.add(Calendar.MONTH, -2);
                break;
            case 12:
                cal.add(Calendar.MONTH, -1);
                break;
            case 26:
                cal.add(Calendar.DAY_OF_YEAR, -14);
                break;
            case 24:
                //ToDo implement better logic for semi-monthly
                cal.add(Calendar.DAY_OF_YEAR, -14);
                break;
            case 52:
                cal.add(Calendar.DAY_OF_YEAR, -7);
                break;
            default:
                Log.d(TAG, "Bad value");
                break;
        }

        Date newDate = cal.getTime();

        return newDate;
    }

    @Override
    public String toString() {
        switch (this.yearlyFrequency ) {
            case 0:
                return "One-time";
            case 1:
                return "Annually";
            case 2:
                return "Semi-annually";
            case 4:
                return "Quarterly";
            case 6:
                return "Bimonthly";
            case 12:
                return "Monthly";
            case 26:
                return "Biweekly";
            case 24:
                return "Semi-monthly";
            case 52:
                return "Weekly";
            default:
                return "Bad value";

        }
    }

}
