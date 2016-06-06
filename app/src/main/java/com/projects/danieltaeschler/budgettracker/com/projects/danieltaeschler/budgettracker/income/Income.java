package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income;

import android.util.Log;

import com.projects.danieltaeschler.budgettracker.Frequency;
import com.projects.danieltaeschler.budgettracker.utilities.DateAmountPair;
import com.projects.danieltaeschler.budgettracker.utilities.DateFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Daniel Taeschler on 12/15/2015.
 */
public class Income {
    private static final String TAG = "Income";

    //JSON titles
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_COST = "cost";
    private static final String JSON_COLLECT_DATE = "collection_date";
    private static final String JSON_FREQUENCY = "frequency";

    private UUID mIncomeId;
    private String mIncomeTitle;
    private Double mIncomeAmount;
    private Date mIncomeCollectionDate;
    private Frequency mIncomeFrequency;

    public Income() {
        mIncomeId = UUID.randomUUID();
        mIncomeAmount = Double.NaN;
        mIncomeCollectionDate = new Date();
        mIncomeFrequency = Frequency.ANNUALLY;
    }

    public Income(String title, double amount) {
        mIncomeId = UUID.randomUUID();
        mIncomeTitle = title;
        mIncomeAmount = amount;
    }

    public Income(JSONObject json) throws JSONException {
        mIncomeId = UUID.fromString(json.getString(JSON_ID));
        mIncomeTitle = json.getString(JSON_TITLE);
        mIncomeAmount = Double.parseDouble(json.getString(JSON_COST));
        mIncomeFrequency = Frequency.valueOf(json.getInt(JSON_FREQUENCY));
        Log.d(TAG, mIncomeTitle + " has a frequency of: " + mIncomeFrequency.toString());
        Date currentDate = new Date(json.getLong(JSON_COLLECT_DATE));
        mIncomeCollectionDate = getNextIncomeCollectionDate(mIncomeFrequency, currentDate);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(JSON_ID, mIncomeId.toString());
        json.put(JSON_TITLE, mIncomeTitle);
        json.put(JSON_COST, mIncomeAmount.toString());
        json.put(JSON_COLLECT_DATE, mIncomeCollectionDate.getTime());
        json.put(JSON_FREQUENCY, mIncomeFrequency.getYearlyFrequency());

        Log.d(TAG, json.toString());
        return json;
    }

    public UUID getIncomeId() {
        return mIncomeId;
    }

    public String getIncomeTitle() {

        return mIncomeTitle;
    }

    public void setIncomeTitle(String mIncomeTitle) {
        this.mIncomeTitle = mIncomeTitle;
    }

    public Double getIncomeAmount() {
        return mIncomeAmount;
    }

    public Double getMonthlyIncomeAmount() { return (mIncomeFrequency.getYearlyFrequency() * mIncomeAmount) / 12; }

    public ArrayList<DateAmountPair> getAllMonthlyIncomes() {
        ArrayList<DateAmountPair> incomes = new ArrayList<>();
        Date currentDate = new Date();
        Date collectionDate = getFirstCollectionDateOfMonth(mIncomeFrequency, mIncomeCollectionDate, currentDate);

        Log.d(TAG, "Last day of month: " + DateFormatter.returnLastDayOfMonth(currentDate));

        while( collectionDate.before(Frequency.subtractFrequencyFromDate(mIncomeFrequency, DateFormatter.returnLastDayOfMonth(currentDate))) ) {
            DateAmountPair pair = new DateAmountPair(collectionDate, mIncomeAmount);
            incomes.add(pair);
            collectionDate = Frequency.addFrequencyToDate(mIncomeFrequency, collectionDate);
            Log.d(TAG, "Adding pair... Date = " + DateFormatter.returnSimpleDate(collectionDate) + ". Amount = " + mIncomeAmount);
        }

        return incomes;
    }

    public String getIncomeAmountString() {return "$" + mIncomeAmount; }

    public void setIncomeAmount(Double mIncomeAmount) {
        this.mIncomeAmount = mIncomeAmount;
    }

    public Date getIncomeCollectionDate() { return mIncomeCollectionDate; }

    public Date getNextIncomeCollectionDate(Frequency frequency, Date incomeDate) {
        Date currentDate = new Date();

        Log.d(TAG, "Receiving date for: " + mIncomeTitle);
        Log.d(TAG, "Current date: " + DateFormatter.returnSimpleDateTime(currentDate));

        while (incomeDate.before(currentDate)) {
            Log.d(TAG, "Tested date: " + DateFormatter.returnSimpleDateTime(incomeDate));
            incomeDate = Frequency.addFrequencyToDate(frequency, incomeDate);
        }

        Log.d(TAG, "Returning date: " + DateFormatter.returnSimpleDate(incomeDate));

        return incomeDate;
    }

    public Date getFirstCollectionDateOfMonth(Frequency frequency, Date incomeDate, Date monthDate) {

        Date firstOfMoth = DateFormatter.setTimeToMidnight(DateFormatter.returnFirstDayOfMonth(monthDate));

        while (incomeDate.after(firstOfMoth)) {
            incomeDate = Frequency.subtractFrequencyFromDate(frequency, incomeDate);
        }

        return incomeDate;
    }

    public void setIncomeCollectionDate(Date date) { mIncomeCollectionDate = date; }

    public Frequency getIncomeFrequency() { return this.mIncomeFrequency; }

    public int getIncomeFrequencyInt() { return this.mIncomeFrequency.getYearlyFrequency(); }

    public void setIncomeFrequency (Frequency frequency) { this.mIncomeFrequency = frequency; }
}
