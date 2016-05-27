package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.projects.danieltaeschler.budgettracker.Budget;
import com.projects.danieltaeschler.budgettracker.Frequency;
import com.projects.danieltaeschler.budgettracker.FrequencyArrayAdapter;
import com.projects.danieltaeschler.budgettracker.R;
import com.projects.danieltaeschler.budgettracker.dialogs.DatePickerFragment;
import com.projects.danieltaeschler.budgettracker.utilities.DateFormatter;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Daniel Taeschler on 12/21/2015.
 */


public class ExpenseEditFragment extends Fragment {
    private static final String TAG = "ExpenseEditFragment";

    private static final String DIALOG_DATE = "date";

    private static final int REQUEST_DATE = 0;

    private Expense mExpense;

    private EditText mExpenseTitleEdit;
    private EditText mExpenseAmountEdit;
    private Button mExpensePayDateEdit;
    private Spinner mExpenseFrequencyEdit;
    private Button mSaveButton;
    private Button mCancelButton;
    private boolean mNewExpense;


    private void updateDate(Date date) {
        mExpensePayDateEdit.setText(DateFormatter.returnSimpleDate(date));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID expenseId = (UUID)getActivity().getIntent().getSerializableExtra(ExpenseListFragment.EXTRA_EXPENSE_ID);
        mExpense = Budget.get(getActivity()).getExpense(expenseId);
        mNewExpense = getActivity().getIntent().getBooleanExtra(ExpenseListFragment.EXTRA_NEW_EXPENSE, false);
        Log.d(TAG, "mNewExpense = " + mNewExpense);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set layout
        View v = inflater.inflate(R.layout.fragment_expense_edit, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && NavUtils.getParentActivityName(getActivity()) != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mExpenseTitleEdit = (EditText)v.findViewById(R.id.expense_title_edit);
        mExpenseTitleEdit.setText(mExpense.getExpenseTitle());

        mExpenseAmountEdit = (EditText)v.findViewById(R.id.expense_amount_edit);
        if (!Double.isNaN(mExpense.getExpenseCost())) {
            mExpenseAmountEdit.setText(Double.toString(mExpense.getExpenseCost()));
        } else {
            mExpenseAmountEdit.setText("0");
        }

        mExpensePayDateEdit = (Button)v.findViewById(R.id.expense_pay_date_edit);
        updateDate(mExpense.getExpensePayDate());
        mExpensePayDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mExpense.getExpensePayDate());
                dialog.setTargetFragment(ExpenseEditFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mExpenseFrequencyEdit = (Spinner)v.findViewById(R.id.expense_frequency_edit);
        final FrequencyArrayAdapter frequencySpinnerAdapter = new FrequencyArrayAdapter(this.getActivity(), Frequency.getFrequenciesArrayList());
        mExpenseFrequencyEdit.setAdapter(frequencySpinnerAdapter);
        int spinnerPosition = frequencySpinnerAdapter.getPosition(mExpense.getExpenseFrequency());
        mExpenseFrequencyEdit.setSelection(spinnerPosition);

        mSaveButton = (Button)v.findViewById(R.id.edit_item_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (  !mExpenseTitleEdit.getText().toString().equals("") && !mExpenseAmountEdit.getText().toString().equals("0")  ) {
                    getActivity().setResult(Activity.RESULT_OK);
                    Log.d(TAG, "Expense title: " + mExpenseTitleEdit.getText().toString() + ".");
                    Log.d(TAG, "Expense cost: " + mExpenseAmountEdit.getText().toString() + ".");
                    mExpense.setExpenseTitle(mExpenseTitleEdit.getText().toString());
                    mExpense.setExpenseCost(Double.parseDouble(mExpenseAmountEdit.getText().toString()));
                    mExpense.setExpenseFrequency(frequencySpinnerAdapter.getItem(mExpenseFrequencyEdit.getSelectedItemPosition()));
                    mExpense.setExpensePayDate(DateFormatter.parseDateFromString(mExpense.getExpensePayDate(), mExpensePayDateEdit.getText().toString()));
                    NavUtils.navigateUpFromSameTask(getActivity());
                } else {
                    Log.d(TAG, "Expense title: " + mExpenseTitleEdit.getText().toString() + ".");
                    Log.d(TAG, "Expense cost: " + mExpenseAmountEdit.getText().toString() + ".");
                    Toast.makeText(getActivity(), "Not all details entered.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCancelButton = (Button)v.findViewById(R.id.edit_item_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewExpense) {
                    Budget.get(getActivity()).deleteExpense(mExpense);
                }
                getActivity().setResult(Activity.RESULT_CANCELED);
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Budget.get(getActivity()).saveExpenses();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If result code is not OK, do not make any changes
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            updateDate(date);
        }
    }

    @Override
      public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_expense, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    if (mNewExpense && ( mExpense.getExpenseTitle() == null || mExpense.getExpenseCost() == Double.NaN)) {
                        Toast.makeText(getActivity(), "Not all details entered. Expense will not be added.", Toast.LENGTH_SHORT).show();
                        Budget.get(getActivity()).deleteExpense(mExpense);
                        NavUtils.navigateUpFromSameTask(getActivity());
                    } else {
                        NavUtils.navigateUpFromSameTask(getActivity());
                    }

                }
                return true;
            case R.id.menu_item_expenseFragment_delete:
                Budget budget = Budget.get(getActivity());
                budget.deleteExpense(mExpense);
                NavUtils.navigateUpFromSameTask(getActivity());
            default:
                return false;
        }
    }

}
