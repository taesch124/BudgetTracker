package com.projects.danieltaeschler.budgettracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense.ExpenseListActivity;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income.IncomeListActivity;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.monthlyreport.BudgetReportActivity;
import com.projects.danieltaeschler.budgettracker.data.BudgetDatabase;

/**
 * Created by Daniel Taeschler on 12/18/2015.
 */
public class MenuFragment extends Fragment {

    private Button mViewExpensesButton;
    private Button mViewIncomesButton;
    private Button mViewBudgetReportButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Budget.get(getActivity()).openDatabase();
        BudgetDatabase.cleanDatabase();
        Budget.get(getActivity()).logBudgetFromDatabase();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set layout
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        mViewExpensesButton = (Button)v.findViewById(R.id.menu_view_expenses_button);
        mViewExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ExpenseListActivity.class);
                startActivity(i);
            }
        });

        mViewIncomesButton = (Button)v.findViewById(R.id.menu_view_incomes_button);
        mViewIncomesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), IncomeListActivity.class);
                startActivity(i);
            }
        });

        mViewBudgetReportButton = (Button)v.findViewById(R.id.menu_view_report_button);
        mViewBudgetReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BudgetReportActivity.class);
                startActivity(i);
            }
        });

        return v;
    }
}
