package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense;

import android.support.v4.app.Fragment;

import com.projects.danieltaeschler.budgettracker.SingleFragmentActivity;

public class ExpenseActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ExpenseFragment();
        //UUID expenseId = (UUID)getIntent().getSerializableExtra(ExpenseFragment.EXTRA_EXPENSE_ID);
        //return ExpenseFragment.newInstance(expenseId);
    }
}
