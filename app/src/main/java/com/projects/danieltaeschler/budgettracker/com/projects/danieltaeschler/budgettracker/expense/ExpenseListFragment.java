package com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
 * Created by Daniel Taeschler on 12/16/2015.
 */
public class ExpenseListFragment extends ListFragment{
    public static final String EXTRA_EXPENSE_ID = "com.projects.danieltaeschler.budgettracker.expense.expense_id";
    public static final String EXTRA_NEW_EXPENSE = "com.projects.danieltaeschler.budgettracker.expense.new_expense";
    private static final String TAG = "ExpenseListFragment";

    private static final int EXPENSE_EDITED = 0;

    private ArrayList<Expense> mExpenses;
    private ExpenseAdapter mExpenseAdapter;
    private ListView mListView;
    private FloatingActionButton mNewExpenseFloatingActionButton;
    private Button mNewExpenseButton;

    public void newExpenseIntent(Expense expense, boolean newExpense) {
        Intent i = new Intent(getActivity(), ExpenseEditActivity.class);
        i.putExtra(EXTRA_EXPENSE_ID, expense.getExpenseId());
        i.putExtra(EXTRA_NEW_EXPENSE, newExpense);
        startActivityForResult(i, EXPENSE_EDITED);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle("Expenses");
        mExpenses = Budget.get(getActivity()).getExpenses();

        mExpenseAdapter = new ExpenseAdapter(mExpenses);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_expense_list, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && NavUtils.getParentActivityName(getActivity()) != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mListView = (ListView)v.findViewById(android.R.id.list);
        mListView.setAdapter(mExpenseAdapter);

        mNewExpenseButton = (Button)v.findViewById(R.id.expense_list_emptyView_newExpenseButton);
        mNewExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Expense expense = new Expense();
                Budget.get(getActivity()).addExpense(expense);
                newExpenseIntent(expense, true);
            }
        });

        mNewExpenseFloatingActionButton = (FloatingActionButton)v.findViewById(R.id.expense_list_newExpense_floatingActionButton);
        mNewExpenseFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Expense expense = new Expense();
                Budget.get(getActivity()).addExpense(expense);
                newExpenseIntent(expense, true);
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
        mExpenses = Budget.get(getActivity()).getExpenses();
        mExpenseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //get the expense from the adapter
        Expense e = mExpenseAdapter.getItem(position);
        Log.d(TAG, "Expense: " + e.getExpenseTitle() + " clicked.");
        Log.d(TAG, "Expense ID: " + e.getExpenseId());

        //Start ExpenseActivity
        Intent i = new Intent(getActivity(), ExpenseActivity.class);
        i.putExtra(EXTRA_EXPENSE_ID, e.getExpenseId());
        startActivity(i);
    }

    private class ExpenseAdapter extends ArrayAdapter<Expense> {
        public ExpenseAdapter(ArrayList<Expense> expenses) { super(getActivity(), 0, expenses); }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //if a view was not passed in, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_expense, null);
            }

            if (convertView.isSelected()) {
                convertView.setBackgroundColor(Color.BLACK);
            }

            //configure view for the specific expenses
            Expense e = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.expense_list_item_title);
            titleTextView.setText(e.getExpenseTitle());

            TextView amountTextView = (TextView)convertView.findViewById(R.id.expense_list_item_amount);
            amountTextView.setText(e.getExpenseCostString());

            TextView datePaidTextView = (TextView)convertView.findViewById(R.id.expense_list_item_payDate);
            datePaidTextView.setText(DateFormatter.returnLongDate(e.getExpensePayDate()));

            return convertView;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.expense_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        Expense expense = mExpenseAdapter.getItem(position);

        switch(item.getItemId()) {
            case R.id.menu_item_edit_expense:
                newExpenseIntent(expense, false);
                return true;
            case R.id.menu_item_delete_expense:
                Budget budget = Budget.get(getActivity());
                budget.get(getActivity()).deleteExpense(expense);
                mExpenses.remove(expense);
                mExpenseAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void setContextMenu(final ListView listView) {

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int selectedCount;
            private Expense expense;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    selectedCount++;
                } else {
                    selectedCount--;
                }

                if (selectedCount == 1) {
                    for (int j = mExpenseAdapter.getCount() - 1; j >= 0; j--) {
                        if (listView.isItemChecked(j)) {
                            expense = mExpenseAdapter.getItem(j);
                        }
                    }
                } else {
                    expense = null;
                }

                Log.d(TAG, "Selected items: " + selectedCount);

                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.expense_list_item_context, menu);
                selectedCount = 0;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                if (selectedCount == 1) {
                    MenuItem item = menu.findItem(R.id.menu_item_edit_expense);
                    item.setVisible(true);
                    return true;
                } else {
                    MenuItem item = menu.findItem(R.id.menu_item_edit_expense);
                    item.setVisible(false);
                    return true;
                }
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_edit_expense:
                        Intent i = new Intent(getActivity(), ExpenseEditActivity.class);
                        i.putExtra(EXTRA_EXPENSE_ID, expense.getExpenseId());
                        startActivityForResult(i, EXPENSE_EDITED);
                        mode.finish();
                        return true;
                    case R.id.menu_item_delete_expense:
                        Budget budget = Budget.get(getActivity());
                        for (int j = mExpenseAdapter.getCount() - 1; j >= 0; j--) {
                            if (listView.isItemChecked(j)) {
                                budget.deleteExpense(mExpenseAdapter.getItem(j));
                                mExpenses.remove(mExpenseAdapter.getItem(j));
                            }
                        }
                        budget.saveExpenses();
                        mExpenseAdapter.notifyDataSetChanged();
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
        if (requestCode == EXPENSE_EDITED) {
            if (resultCode == Activity.RESULT_OK) {
                mExpenseAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Expense edited.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Expense editing canceled.", Toast.LENGTH_SHORT).show();
            } else {
                return;
            }
        }
    }
}
