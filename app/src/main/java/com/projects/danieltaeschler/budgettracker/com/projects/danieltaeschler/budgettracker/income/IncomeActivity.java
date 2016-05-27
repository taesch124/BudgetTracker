package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income;

import android.support.v4.app.Fragment;

import com.projects.danieltaeschler.budgettracker.SingleFragmentActivity;

/**
 * Created by Daniel Taeschler on 12/20/2015.
 */
public class IncomeActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new IncomeFragment();
    }

}
