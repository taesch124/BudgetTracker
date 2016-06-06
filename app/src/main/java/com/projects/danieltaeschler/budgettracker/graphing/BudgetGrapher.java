package com.projects.danieltaeschler.budgettracker.graphing;

import android.content.Context;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.projects.danieltaeschler.budgettracker.Budget;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense.Expense;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income.Income;
import com.projects.danieltaeschler.budgettracker.utilities.DateAmountPair;
import com.projects.danieltaeschler.budgettracker.utilities.DateFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel Taeschler on 6/3/2016.
 */
public class BudgetGrapher {
    private static final String TAG = "BudgetGrapher";

    private Context mContext;
    private ArrayList<Expense> mExpenses;
    private ArrayList<Income> mIncomes;

    public BudgetGrapher (Context context) {
        mContext = context;
        mExpenses  = Budget.get(mContext).getExpenses();
        mIncomes = Budget.get(mContext).getIncomes();
    }

    public LineGraphSeries<DataPoint> createMonthlyBudgetReportLineGraph() {
        ArrayList<DateAmountPair> incomeDateAmountPairs = new ArrayList<>();
        ArrayList<DateAmountPair> expenseDateAmountPairs = new ArrayList<>();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateFormatter.setTimeToMidnight(currentDate));
        int monthLength = DateFormatter.returnIntLastDayOfMonth(currentDate);

        double totalAmount = 0;

        //find all previous payments of the current month
        for (Income income : mIncomes) {
            incomeDateAmountPairs.addAll(income.getAllMonthlyIncomes());
        }

        Log.d(TAG, "income pair size: " + incomeDateAmountPairs.size());

        for (Expense expense : mExpenses) {
            expenseDateAmountPairs.addAll(expense.getAllMonthlyExpenses());
        }

        Log.d(TAG, "expense pair size: " + expenseDateAmountPairs.size());

        DataPoint[] dataPoints = new DataPoint[monthLength];

        for (int i = 0; i < monthLength ; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i+1);
            Log.d(TAG, "Testing date: " + DateFormatter.returnSimpleDateTime(calendar.getTime()));

            for (DateAmountPair incomePair : incomeDateAmountPairs) {
                if ( DateFormatter.setTimeToMidnight(incomePair.getDate()).equals(calendar.getTime()) ) {
                    totalAmount += incomePair.getAmount();
                    Log.d(TAG, "Adding amount: " + incomePair.getAmount() + " for date " + incomePair.getDate());
                }
            }

            for (DateAmountPair expensePair : expenseDateAmountPairs) {
                if ( DateFormatter.setTimeToMidnight(expensePair.getDate()).equals(calendar.getTime()) ) {
                    totalAmount -= expensePair.getAmount();
                    Log.d(TAG, "Subtracting amount: " + expensePair.getAmount() + " for date " + expensePair.getDate());
                }
            }

            dataPoints[i] = new DataPoint(i+1, totalAmount);
        }

        for (int i = 0; i < dataPoints.length; i++) {
            Log.d(TAG, "(" + dataPoints[i].getX() + ", " + dataPoints[i].getY() + ")");
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        return series;
    }


}
