package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense;

import android.support.v4.app.Fragment;

import com.projects.danieltaeschler.budgettracker.SingleFragmentActivity;

/**
 * Created by Daniel Taeschler on 12/21/2015.
 */
public class ExpenseEditActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new ExpenseEditFragment();
    }
}
