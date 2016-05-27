package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income;

import android.support.v4.app.Fragment;

import com.projects.danieltaeschler.budgettracker.SingleFragmentActivity;

/**
 * Created by Daniel Taeschler on 12/21/2015.
 */
public class IncomeListActivity extends SingleFragmentActivity {

    //attach a new ExpenseListFragment to this activity
    @Override
    protected Fragment createFragment() {
        return new IncomeListFragment();
    }
}
