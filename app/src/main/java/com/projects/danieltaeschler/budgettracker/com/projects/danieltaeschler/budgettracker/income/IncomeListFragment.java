package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.danieltaeschler.budgettracker.Budget;
import com.projects.danieltaeschler.budgettracker.R;
import com.projects.danieltaeschler.budgettracker.utilities.DateFormatter;

import java.util.ArrayList;

/**
 * Created by Daniel Taeschler on 12/21/2015.
 */
public class IncomeListFragment extends ListFragment {
    private static final String TAG = "IncomeListFragment";
    public static final String EXTRA_INCOME_ID = "com.projects.danieltaeschler.budgettracker.income.income_id";
    public static final String EXTRA_NEW_INCOME = "com.projects.danieltaeschler.budgettracker.income.new_income";

    private static final int INCOME_EDITED = 0;


    private ArrayList<Income> mIncomes;
    private IncomeAdapter mIncomeAdapter;
    private ListView mListView;
    private FloatingActionButton mNewIncomeFloatingActionButton;
    private Button mNewIncomeButton;

    public void newIncomeIntent(Income income, boolean newIncome) {
        Intent i = new Intent(getActivity(), IncomeEditActivity.class);
        i.putExtra(EXTRA_INCOME_ID, income.getIncomeId());
        i.putExtra(EXTRA_NEW_INCOME, newIncome);
        startActivityForResult(i, INCOME_EDITED);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle("Incomes");
        mIncomes = Budget.get(getActivity()).getIncomes();

        mIncomeAdapter = new IncomeAdapter(mIncomes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_income_list, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && NavUtils.getParentActivityName(getActivity()) != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mListView = (ListView)v.findViewById(android.R.id.list);
        mListView.setAdapter(mIncomeAdapter);

        mNewIncomeButton = (Button)v.findViewById(R.id.income_list_emptyView_newIncomeButton);
        mNewIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Income income = new Income();
                Budget.get(getActivity()).addIncome(income);
                newIncomeIntent(income, true);
            }
        });

        mNewIncomeFloatingActionButton = (FloatingActionButton)v.findViewById(R.id.income_list_newIncome_floatingActionButton);
        mNewIncomeFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Income income = new Income();
                Budget.get(getActivity()).addIncome(income);
                newIncomeIntent(income, true);
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(mListView);
        } else {
            setContextMenu(mListView);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIncomeAdapter.notifyDataSetChanged();
        Budget.get(getActivity()).saveIncomes();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //get the incomes from the adapter
        Income i = mIncomeAdapter.getItem(position);
        Log.d(TAG, "Income: " + i.getIncomeTitle() + " clicked.");
        Log.d(TAG, "Income ID: " + i.getIncomeId());

        //Start IncomeActivity
        Intent intent = new Intent(getActivity(), IncomeActivity.class);
        intent.putExtra(EXTRA_INCOME_ID, i.getIncomeId());
        startActivity(intent);
    }


    private class IncomeAdapter extends ArrayAdapter<Income> {
        public IncomeAdapter(ArrayList<Income> incomes) { super(getActivity(), 0, incomes); }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //if a view was not passed in, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_income, null);
            }

            //configure view for the specific incomes
            Income i = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.income_list_item_title);
            titleTextView.setText(i.getIncomeTitle());

            TextView amountTextView = (TextView)convertView.findViewById(R.id.income_list_item_amount);
            amountTextView.setText(i.getIncomeAmountString());

            TextView datePaidTextView = (TextView)convertView.findViewById(R.id.income_list_item_payDate);
            datePaidTextView.setText(DateFormatter.returnLongDate(i.getIncomeCollectionDate()));

            return convertView;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.income_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        Income income = mIncomeAdapter.getItem(position);

        switch(item.getItemId()) {
            case R.id.menu_item_edit_income:
                newIncomeIntent(income, false);
                return true;
            case R.id.menu_item_delete_income:
                Budget budget = Budget.get(getActivity());
                budget.deleteIncome(income);
                budget.saveIncomes();
                mIncomeAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void setContextMenu(final ListView listView) {

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int selectedCount;
            private Income income;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    selectedCount++;
                } else {
                    selectedCount--;
                }

                if (selectedCount == 1) {
                    for (int j = mIncomeAdapter.getCount() - 1; j >= 0; j--) {
                        if (listView.isItemChecked(j)) {
                            income = mIncomeAdapter.getItem(j);
                        }
                    }
                } else {
                    income = null;
                }

                Log.d(TAG, "Selected items: " + selectedCount);

                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.income_list_item_context, menu);
                selectedCount = 0;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                if (selectedCount == 1) {
                    MenuItem item = menu.findItem(R.id.menu_item_edit_income);
                    item.setVisible(true);
                    return true;
                } else {
                    MenuItem item = menu.findItem(R.id.menu_item_edit_income);
                    item.setVisible(false);
                    return true;
                }
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_edit_income:
                        Intent i = new Intent(getActivity(), IncomeEditActivity.class);
                        i.putExtra(EXTRA_INCOME_ID, income.getIncomeId());
                        startActivityForResult(i, INCOME_EDITED);
                        mode.finish();
                        return true;
                    case R.id.menu_item_delete_income:
                        Budget budget = Budget.get(getActivity());
                        for (int j = mIncomeAdapter.getCount() - 1; j >= 0; j--) {
                            if (listView.isItemChecked(j)) {
                                budget.deleteIncome(mIncomeAdapter.getItem(j));
                            }
                        }
                        budget.saveIncomes();
                        mIncomeAdapter.notifyDataSetChanged();
                        mode.finish();
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INCOME_EDITED) {
            if (resultCode == Activity.RESULT_OK) {
                mIncomeAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Income edited.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Income editing canceled.", Toast.LENGTH_SHORT).show();
            } else {
                return;
            }
        }
    }

}
