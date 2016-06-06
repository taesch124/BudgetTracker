package com.projects.danieltaeschler.budgettracker.utilities;

import java.text.DecimalFormat;

/**
 * Created by Daniel Taeschler on 12/15/2015.
 */
public class DecimalFormatter {
    private static DecimalFormat currencyFormatter = new DecimalFormat("#.##");


    public static String getCurrencyFormatString(double num) {
        return currencyFormatter.format(num);
    }

}
