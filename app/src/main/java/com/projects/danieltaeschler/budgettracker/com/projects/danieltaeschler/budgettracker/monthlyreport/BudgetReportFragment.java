package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.monthlyreport;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.projects.danieltaeschler.budgettracker.Budget;
import com.projects.danieltaeschler.budgettracker.R;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense.Expense;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income.Income;
import com.projects.danieltaeschler.budgettracker.graphing.BudgetGrapher;
import com.projects.danieltaeschler.budgettracker.utilities.DateFormatter;

import java.util.Date;

/**
 * Created by Daniel Taeschler on 5/15/2016.
 */
public class BudgetReportFragment extends Fragment {
    private static final String TAG = "BudgetReportFragment";

    private BudgetGrapher mGrapher;

    private TextView mIncomeTotalTextView;
    private TextView mExpenseTotalTextView;
    private GraphView mMonthlyReportGraph;
    private Date currentDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGrapher = new BudgetGrapher(getActivity());
        currentDate = new Date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set layout
        View v = inflater.inflate(R.layout.fragment_monthly_report, container, false);

        mMonthlyReportGraph = (GraphView)v.findViewById(R.id.fragment_monthly_report_graph);
        //mMonthlyReportGraph = mGrapher.createMonthlyBudgetReportLineGraph();
        LineGraphSeries<DataPoint> series = mGrapher.createMonthlyBudgetReportLineGraphSeries();
        mMonthlyReportGraph.addSeries(series);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);
        mMonthlyReportGraph.setTitle(DateFormatter.getMonthNameString(currentDate) + " Budget Outlook");
        mMonthlyReportGraph.getViewport().setXAxisBoundsManual(true);
        mMonthlyReportGraph.getViewport().setMinX(1);
        mMonthlyReportGraph.getViewport().setMaxX(DateFormatter.returnIntLastDayOfMonth(currentDate));
        mMonthlyReportGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        mMonthlyReportGraph.getGridLabelRenderer().setNumHorizontalLabels(3);

        mIncomeTotalTextView = (TextView)v.findViewById(R.id.fragment_total_income_amount);
        mIncomeTotalTextView.setText(Double.toString(Budget.get(getActivity()).getCurrentMonthIncome()));

        mExpenseTotalTextView = (TextView)v.findViewById(R.id.fragment_total_expense_amount);
        mExpenseTotalTextView.setText(Double.toString(Budget.get(getActivity()).getCurrentMonthExpense()));

        return v;
    }

}
