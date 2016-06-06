package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense;

import android.util.Log;

import com.projects.danieltaeschler.budgettracker.Frequency;
import com.projects.danieltaeschler.budgettracker.utilities.DateAmountPair;
import com.projects.danieltaeschler.budgettracker.utilities.DateFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.lang.Math;

/**
 * Created by Daniel Taeschler on 12/12/2015.
 */
public class Expense {
    private static final String TAG = "Expense";

    //JSON titles
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_COST = "cost";
    private static final String JSON_DATE = "pay_date";
    private static final String JSON_FREQUENCY = "frequency";

    //will assume monthly expenses for now
    private UUID mExpenseId;
    private String mExpenseTitle;
    private Double mExpenseCost;
    private Date mExpensePayDate;
    private Frequency mExpenseFrequency;

    public Expense() {
        mExpenseId = UUID.randomUUID();
        mExpenseCost = Double.NaN;
        mExpensePayDate = new Date();
        mExpenseFrequency = Frequency.ANNUALLY;
    }

    public Expense(JSONObject json) throws JSONException {
        mExpenseId = UUID.fromString(json.getString(JSON_ID));
        mExpenseTitle = json.getString(JSON_TITLE);
        mExpenseCost = Double.parseDouble(json.getString(JSON_COST));
        mExpenseFrequency = Frequency.valueOf(json.getInt(JSON_FREQUENCY));
        Date currentDate = new Date(json.getLong(JSON_DATE));
        mExpensePayDate = getNextExpensePayDate(mExpenseFrequency, currentDate);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(JSON_ID, mExpenseId.toString());
        json.put(JSON_TITLE, mExpenseTitle);
        json.put(JSON_COST, mExpenseCost.toString());
        json.put(JSON_DATE, mExpensePayDate.getTime());
        json.put(JSON_FREQUENCY, mExpenseFrequency.getYearlyFrequency());

        Log.d(TAG, json.toString());

        return json;
    }

    public UUID getExpenseId() {
        return mExpenseId;
    }

    public String getExpenseTitle() {
        return mExpenseTitle;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.mExpenseTitle = expenseTitle;
    }

    public double getExpenseCost() { return mExpenseCost; }

    public Double getMonthlyExpenseCost() { return (mExpenseFrequency.getYearlyFrequency() * mExpenseCost) / 12; }

    public ArrayList<DateAmountPair> getAllMonthlyExpenses() {
        ArrayList<DateAmountPair> expenses = new ArrayList<>();
        Date currentDate = new Date();
        Date collectionDate = getFirstExpenseDateOfMonth(mExpenseFrequency, mExpensePayDate, currentDate);

        Log.d(TAG, "Last day of month: " + DateFormatter.returnLastDayOfMonth(currentDate));

        while( collectionDate.before(DateFormatter.returnLastDayOfMonth(currentDate)) ) {
            DateAmountPair pair = new DateAmountPair(collectionDate, mExpenseCost);
            expenses.add(pair);
            collectionDate = Frequency.addFrequencyToDate(mExpenseFrequency, collectionDate);
            Log.d(TAG, "Adding pair... Date = " + DateFormatter.returnSimpleDate(collectionDate) + ". Amount = " + mExpenseCost);
        }

        return expenses;
    }

    public String getExpenseCostString() {
        return "$" + mExpenseCost.toString();
    }

    public void setExpenseCost(double expenseCost) {

        if (expenseCost < 0) {
            this.mExpenseCost = Math.abs(expenseCost);
        } else {
            this.mExpenseCost = expenseCost;
        }
    }

    public Date getExpensePayDate() { return mExpensePayDate; }

    public Date getNextExpensePayDate(Frequency frequency, Date expenseDate) {
        Date currentDate = new Date();

        while (expenseDate.before(currentDate)) {
            expenseDate = Frequency.addFrequencyToDate(frequency, expenseDate);
        }

        return expenseDate;
    }

    public Date getFirstExpenseDateOfMonth(Frequency frequency, Date expenseDate, Date monthDate) {

        Date firstOfMoth = DateFormatter.setTimeToMidnight(DateFormatter.returnFirstDayOfMonth(monthDate));

        while (expenseDate.after(firstOfMoth)) {
            expenseDate = Frequency.subtractFrequencyFromDate(frequency, expenseDate);
        }

        return expenseDate;
    }

    public void setExpensePayDate(Date date) { mExpensePayDate = date; }

    public Frequency getExpenseFrequency() {return mExpenseFrequency; }

    public int getExpenseFrequencyInt() {return mExpenseFrequency.getYearlyFrequency();}

    public void setExpenseFrequency(Frequency frequency) {this.mExpenseFrequency = frequency; }

}
