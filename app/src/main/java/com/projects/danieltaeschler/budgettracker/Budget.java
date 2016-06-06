package com.projects.danieltaeschler.budgettracker;

import android.content.Context;
import android.util.Log;

import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense.Expense;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income.Income;
import com.projects.danieltaeschler.budgettracker.utilities.BudgetTrackerJSONSerializer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Daniel Taeschler on 12/16/2015.
 */
public class Budget {
    private static final String TAG = "Budget";
    private static final String EXPENSES_FILENAME = "budget_expenses.json";
    private static final String INCOMES_FILENAME = "budget_incomes.json";

    public ArrayList<Income> mIncomes;
    public ArrayList<Expense> mExpenses;

    //declaration for singleton
    private static Budget sBudget;
    private Context mAppContext;
    private BudgetTrackerJSONSerializer mSerializer;

    //This budget will be based off of me right now
    private Budget(Context appContext) {
        mAppContext = appContext;

        mIncomes = new ArrayList<>();

        //Create serializer to save Expenses and Incomes
        mSerializer = new BudgetTrackerJSONSerializer(mAppContext, EXPENSES_FILENAME, INCOMES_FILENAME);

        try {
            mExpenses = mSerializer.loadExpenses();
            mIncomes = mSerializer.loadIncomes();
        } catch (Exception e) {
            mExpenses = new ArrayList<>();
            mIncomes = new ArrayList<>();
            Log.e(TAG, "Error loading history. Creating new ArrayLists.", e);
        }
    }

    public double getTotalIncome() {
        double incomeTotal = 0;

        for (Income i : mIncomes) {
            incomeTotal += i.getIncomeAmount();
        }

        Log.d(TAG, "Total income = " + incomeTotal);
        return incomeTotal;
    }

    public double getTotalExpense() {
        double expenseTotal = 0;

        for (Expense e: mExpenses) {
            expenseTotal += e.getExpenseCost();
        }

        Log.d(TAG, "Total expense = " + expenseTotal);
        return expenseTotal;
    }

    public static Budget get(Context c) {
        if (sBudget == null) {
            return sBudget = new Budget(c.getApplicationContext());
        }

        return sBudget;
    }

    public ArrayList<Income> getIncomes() {
        return mIncomes;
    }
    public ArrayList<Expense> getExpenses() {
        return mExpenses;
    }

    public Income getIncome(UUID incomeId) {
        for(Income i : mIncomes) {
            if(i.getIncomeId().equals(incomeId)) {
                return i;
            }
        }
        return null;
    }

    public void deleteIncome(Income income) {
        mIncomes.remove(income);
    }

    public void addIncome (Income income) { mIncomes.add(income); }

    public boolean saveIncomes() {
        try {
            mSerializer.saveIncomes(mIncomes);
            Log.d(TAG, "Incomes saved to file.");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving incomes.");
            return false;
        }
    }

    public Expense getExpense (UUID expenseId) {
        for(Expense e: mExpenses) {
            if(e.getExpenseId().equals(expenseId)) {
                return e;
            }
        }
        return null;
    }

    public void deleteExpense (Expense expense) {
        mExpenses.remove(expense);
    }

    public void addExpense (Expense expense) { mExpenses.add(expense); }

    public boolean saveExpenses() {
        try {
            mSerializer.saveExpenses(mExpenses);
            Log.d(TAG, "Expenses saved to file.");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving expenses.", e);
            return false;
        }
    }

}
