package com.projects.danieltaeschler.budgettracker.utilities;

import android.util.Pair;

import java.util.Date;

/**
 * Created by Daniel Taeschler on 6/3/2016.
 */
public class DateAmountPair{
    private final Date mDate;
    private final Double mAmount;

    public DateAmountPair(Date date, Double amount) {
        this.mDate = date;
        this.mAmount = amount;
    }

    public Date getDate() { return mDate; }

    public Double getAmount() { return mAmount; }

}
