package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income;

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
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Daniel Taeschler on 5/15/2016.
 */
public class IncomeEditFragment extends Fragment {
    private static final String TAG = "ExpenseEditFragment";

    private static final String DIALOG_DATE = "date";

    private static final int REQUEST_DATE = 0;

    private Income mIncome;

    private EditText mIncomeTitleEdit;
    private EditText mIncomeAmountEdit;
    private Button mIncomeCollectDateEdit;
    private Spinner mIncomeFrequencyEdit;
    private Button mSaveButton;
    private Button mCancelButton;
    private boolean mNewIncome;

    private void updateDate(Date date) {
        mIncomeCollectDateEdit.setText(DateFormatter.returnSimpleDate(date));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID incomeId = (UUID)getActivity().getIntent().getSerializableExtra(IncomeListFragment.EXTRA_INCOME_ID);
        mIncome = Budget.get(getActivity()).getIncome(incomeId);
        mNewIncome = getActivity().getIntent().getBooleanExtra(IncomeListFragment.EXTRA_NEW_INCOME, false);
        Log.d(TAG, "mNewExpense = " + mNewIncome);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set layout
        View v = inflater.inflate(R.layout.fragment_income_edit, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && NavUtils.getParentActivityName(getActivity()) != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mIncomeTitleEdit = (EditText)v.findViewById(R.id.income_title_edit);
        mIncomeTitleEdit.setText(mIncome.getIncomeTitle());

        mIncomeAmountEdit = (EditText)v.findViewById(R.id.income_amount_edit);
        if (!Double.isNaN(mIncome.getIncomeAmount())) {
            mIncomeAmountEdit.setText(Double.toString(mIncome.getIncomeAmount()));
        } else {
            mIncomeAmountEdit.setText("0");
        }

        mIncomeCollectDateEdit = (Button)v.findViewById(R.id.income_collect_date_edit);
        updateDate(mIncome.getIncomeCollectionDate());
        mIncomeCollectDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mIncome.getIncomeCollectionDate());
                dialog.setTargetFragment(IncomeEditFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mIncomeFrequencyEdit = (Spinner)v.findViewById(R.id.income_frequency_edit);
        final FrequencyArrayAdapter frequencySpinnerAdapter = new FrequencyArrayAdapter(this.getActivity(), Frequency.getFrequenciesArrayList());
        mIncomeFrequencyEdit.setAdapter(frequencySpinnerAdapter);
        int spinnerPosition = frequencySpinnerAdapter.getPosition(mIncome.getIncomeFrequency());
        mIncomeFrequencyEdit.setSelection(spinnerPosition);

        mSaveButton = (Button)v.findViewById(R.id.edit_item_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !mIncomeTitleEdit.getText().toString().equals("") && !mIncomeAmountEdit.getText().toString().equals("0")) {
                    getActivity().setResult(Activity.RESULT_OK);
                    mIncome.setIncomeTitle(mIncomeTitleEdit.getText().toString());
                    mIncome.setIncomeAmount(Double.parseDouble(mIncomeAmountEdit.getText().toString()));
                    mIncome.setIncomeCollectionDate(DateFormatter.parseDateFromString(mIncome.getIncomeCollectionDate(), mIncomeCollectDateEdit.getText().toString()));
                    mIncome.setIncomeFrequency(frequencySpinnerAdapter.getItem(mIncomeFrequencyEdit.getSelectedItemPosition()));
                    NavUtils.navigateUpFromSameTask(getActivity());
                } else {
                    Toast.makeText(getActivity(), "Not all details entered.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mCancelButton = (Button)v.findViewById(R.id.edit_item_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewIncome) {
                    Budget.get(getActivity()).deleteIncome(mIncome);
                }
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Budget.get(getActivity()).saveIncomes();
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
        menuInflater.inflate(R.menu.fragment_income, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    if (mNewIncome && ( mIncome.getIncomeTitle() == null || mIncome.getIncomeAmount() == Double.NaN)) {
                        Toast.makeText(getActivity(), "Not all details entered. Income will not be added.", Toast.LENGTH_SHORT).show();
                        Budget.get(getActivity()).deleteIncome(mIncome);
                        NavUtils.navigateUpFromSameTask(getActivity());
                    } else {
                        NavUtils.navigateUpFromSameTask(getActivity());
                    }

                }
                return true;
            case R.id.menu_item_incomeFragment_delete:
                Budget budget = Budget.get(getActivity());
                budget.deleteIncome(mIncome);
                NavUtils.navigateUpFromSameTask(getActivity());
            default:
                return false;
        }
    }

}
