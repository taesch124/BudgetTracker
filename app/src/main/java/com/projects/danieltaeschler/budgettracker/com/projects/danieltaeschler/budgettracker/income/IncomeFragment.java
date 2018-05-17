package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.projects.danieltaeschler.budgettracker.Budget;
import com.projects.danieltaeschler.budgettracker.R;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense.Expense;
import com.projects.danieltaeschler.budgettracker.utilities.DateFormatter;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Daniel Taeschler on 12/20/2015.
 */
public class IncomeFragment extends Fragment {
    private static final String TAG = "IncomeFragment";

    private static final int INCOME_EDITED = 0;

    private Income mIncome;
    private ArrayList<Expense> mExpenses;

    private TextView mIncomeTitleField;
    private TextView mIncomeAmountField;
    private TextView mIncomeCollectionDateField;
    private TextView mIncomeFrequencyField;

    public static IncomeFragment newInstance(UUID incomeId) {
        Bundle args = new Bundle();
        args.putSerializable(IncomeListFragment.EXTRA_INCOME_ID, incomeId);

        IncomeFragment fragment = new IncomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mExpenses = Budget.get(getActivity()).getExpenses();

        UUID incomeId = (UUID)getActivity().getIntent().getSerializableExtra(IncomeListFragment.EXTRA_INCOME_ID);
        Log.d(TAG, "Income ID: " + incomeId);
        mIncome = Budget.get(getActivity()).getIncome(incomeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set layout
        View v = inflater.inflate(R.layout.fragment_income, container, false);

        mIncomeTitleField = (TextView)v.findViewById(R.id.income_title);
        mIncomeTitleField.setText(mIncome.getIncomeTitle());

        mIncomeAmountField = (TextView)v.findViewById(R.id.income_amount);
        mIncomeAmountField.setText(mIncome.getIncomeAmountString());

        mIncomeCollectionDateField = (TextView)v.findViewById(R.id.income_next_collection_date);
        mIncomeCollectionDateField.setText(DateFormatter.returnSimpleDate(mIncome.getIncomeCollectionDate()));

        mIncomeFrequencyField = (TextView)v.findViewById(R.id.income_frequency);
        mIncomeFrequencyField.setText(mIncome.getIncomeFrequency().toString());

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_edit_income, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_editIncomeFragment_edit:
                Intent i = new Intent(getActivity(), IncomeEditActivity.class);
                i.putExtra(IncomeListFragment.EXTRA_INCOME_ID, mIncome.getIncomeId());
                startActivityForResult(i, INCOME_EDITED);
                return true;
            case R.id.menu_item_editIncomeFragment_delete:
                Budget budget = Budget.get(getActivity());
                budget.deleteIncome(mIncome);
                getActivity().finish();
                return true;
            default:
                return false;
        }
    }
}
