package com.projects.danieltaeschler.budgettracker;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Daniel Taeschler on 5/17/2016.
 */
public enum Frequency {
    ANNUALLY(1),
    SEMI_ANNUALLY(2),
    QUARTERLY(4),
    MONTHLY(12),
    BIWEEKLY(26),
    SEMI_MONTHLY(24),
    WEEKLY(52);

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

        return Frequency.ANNUALLY;
    }

    @Override
    public String toString() {
        switch (this.yearlyFrequency ) {
            case 1:
                return "Annually";
            case 2:
                return "Semi-annually";
            case 4:
                return "Quarterly";
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
