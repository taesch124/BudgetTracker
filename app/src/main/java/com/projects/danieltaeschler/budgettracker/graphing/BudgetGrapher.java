package com.projects.danieltaeschler.budgettracker.graphing;

import android.content.Context;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
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

    public LineGraphSeries<DataPoint> createMonthlyBudgetReportLineGraphSeries() {
        ArrayList<DateAmountPair> incomeDateAmountPairs = new ArrayList<>();
        ArrayList<DateAmountPair> expenseDateAmountPairs = new ArrayList<>();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateFormatter.setTimeToMidnight(currentDate));
        int monthLength = DateFormatter.returnIntLastDayOfMonth(currentDate);

        double totalAmount = 0;

        //find all incomes/expenses of the current month
        for (Income income : mIncomes) {
            incomeDateAmountPairs.addAll(income.getAllMonthlyIncomes());
        }

        for (Expense expense : mExpenses) {
            expenseDateAmountPairs.addAll(expense.getAllMonthlyExpenses());
        }

        DataPoint[] dataPoints = new DataPoint[monthLength];

        for (int i = 1; i <= monthLength ; i++) {
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

            dataPoints[i-1] = new DataPoint(calendar.getTime(), totalAmount);
            calendar.set(Calendar.DAY_OF_MONTH, i);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        return series;
    }

    public GraphView createMonthlyBudgetReportLineGraph() {
        GraphView monthlyGraphView = new GraphView(mContext);
        Date currentDate = new Date();

        monthlyGraphView.addSeries(createMonthlyBudgetReportLineGraphSeries());
        monthlyGraphView.setTitle(DateFormatter.getMonthNameString(currentDate) + " Budget Outlook");
        monthlyGraphView.getViewport().setMinX(1);
        monthlyGraphView.getViewport().setMaxX(DateFormatter.returnIntLastDayOfMonth(currentDate));

        return monthlyGraphView;
    }


}
