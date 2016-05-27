package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.monthlyreport;

import android.support.v4.app.Fragment;

import com.projects.danieltaeschler.budgettracker.SingleFragmentActivity;

/**
 * Created by Daniel Taeschler on 5/15/2016.
 */
public class BudgetReportActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new BudgetReportFragment();
    }
}
