package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.monthlyreport;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.danieltaeschler.budgettracker.Budget;
import com.projects.danieltaeschler.budgettracker.R;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense.Expense;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income.Income;

/**
 * Created by Daniel Taeschler on 5/15/2016.
 */
public class BudgetReportFragment extends Fragment {
    private static final String TAG = "BudgetReportFragment";

    private TextView mIncomeTotalTextView;
    private TextView mExpenseTotalTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set layout
        View v = inflater.inflate(R.layout.fragment_monthly_report, container, false);

        mIncomeTotalTextView = (TextView)v.findViewById(R.id.fragment_total_income_amount);
        mIncomeTotalTextView.setText(Double.toString(Budget.get(getActivity()).getTotalIncome()));

        mExpenseTotalTextView = (TextView)v.findViewById(R.id.fragment_total_expense_amount);
        mExpenseTotalTextView.setText(Double.toString(Budget.get(getActivity()).getTotalExpense()));

        for (Income i : Budget.get(getActivity()).getIncomes()) {
            Log.d(TAG, "Income title: " + i.getIncomeTitle());
            Log.d(TAG, "Monthly income: " + i.getMonthlyIncomeAmount());
        }

        for (Expense e : Budget.get(getActivity()).getExpenses()) {
            Log.d(TAG, "Expense title: " + e.getExpenseTitle());
            Log.d(TAG, "Monthly cost: " + e.getExpenseMonthlyCost());
        }


        return v;
    }

}
