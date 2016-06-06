package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.danieltaeschler.budgettracker.Budget;
import com.projects.danieltaeschler.budgettracker.utilities.DecimalFormatter;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income.Income;
import com.projects.danieltaeschler.budgettracker.R;
import com.projects.danieltaeschler.budgettracker.utilities.DateFormatter;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Daniel Taeschler on 12/12/2015.
 */
public class ExpenseFragment extends Fragment {
    private static final String TAG = "ExpenseFragment";

    private static final int EXPENSE_EDITED = 0;

    private Expense mExpense;
    private ArrayList<Income> mIncomes;

    private TextView mExpenseTitleField;
    private TextView mExpenseAmountField;
    private TextView mExpenseAmountPercentField;
    private TextView mExpenseFrequencyField;
    private TextView mExpensePayDateField;

    public static ExpenseFragment newInstance(UUID expenseId) {
        Bundle args = new Bundle();
        args.putSerializable(ExpenseListFragment.EXTRA_EXPENSE_ID, expenseId);

        ExpenseFragment fragment = new ExpenseFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mIncomes = Budget.get(getActivity()).getIncomes();

        UUID expenseId = (UUID)getActivity().getIntent().getSerializableExtra(ExpenseListFragment.EXTRA_EXPENSE_ID);
        mExpense = Budget.get(getActivity()).getExpense(expenseId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set layout
        View v = inflater.inflate(R.layout.fragment_expense, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && NavUtils.getParentActivityName(getActivity()) != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //set field values
        mExpenseTitleField = (TextView)v.findViewById(R.id.expense_title);
        mExpenseTitleField.setText(mExpense.getExpenseTitle());


        mExpenseAmountField = (TextView)v.findViewById(R.id.expense_amount);
        mExpenseAmountField.setText(mExpense.getExpenseCostString());

        mExpenseAmountPercentField = (TextView)v.findViewById(R.id.expense_amount_percent);
        mExpenseAmountPercentField.setText( DecimalFormatter.getCurrencyFormatString(Double.valueOf( (mExpense.getExpenseCost() / Budget.get(getActivity()).getTotalIncome()) * 100 )) + "%");

        mExpensePayDateField = (TextView)v.findViewById(R.id.expense_pay_date);
        mExpensePayDateField.setText(DateFormatter.returnSimpleDate(mExpense.getExpensePayDate()));

        mExpenseFrequencyField = (TextView)v.findViewById(R.id.expense_frequency);
        mExpenseFrequencyField.setText( mExpense.getExpenseFrequency().toString());


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_edit_expense, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_editExpenseFragment_edit:
                Intent i = new Intent(getActivity(), ExpenseEditActivity.class);
                i.putExtra(ExpenseListFragment.EXTRA_EXPENSE_ID, mExpense.getExpenseId());
                startActivityForResult(i, EXPENSE_EDITED);
                return true;
            case R.id.menu_item_editExpenseFragment_delete:
                Budget budget = Budget.get(getActivity());
                budget.deleteExpense(mExpense);
                getActivity().finish();
            default:
                return false;
        }
    }
}
