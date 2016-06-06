package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense;

import android.support.v4.app.Fragment;

import com.projects.danieltaeschler.budgettracker.utilities.SingleFragmentActivity;

/**
 * Created by Daniel Taeschler on 12/16/2015.
 */
public class ExpenseListActivity extends SingleFragmentActivity {

    //attach a new ExpenseListFragment to this activity
    @Override
    protected Fragment createFragment() {
        return new ExpenseListFragment();
    }
}
