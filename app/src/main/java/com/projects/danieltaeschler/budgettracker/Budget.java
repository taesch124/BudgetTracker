package com.projects.danieltaeschler.budgettracker;

import android.content.Context;
import android.util.Log;

import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense.Expense;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income.Income;
import com.projects.danieltaeschler.budgettracker.data.BudgetDatabase;
import com.projects.danieltaeschler.budgettracker.utilities.BudgetTrackerJSONSerializer;
import com.projects.danieltaeschler.budgettracker.utilities.DateAmountPair;
import com.projects.danieltaeschler.budgettracker.utilities.DateFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public double getCurrentMonthIncome() {
        ArrayList<DateAmountPair> incomeDateAmountPairs = new ArrayList<>();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateFormatter.setTimeToMidnight(currentDate));
        int monthLength = DateFormatter.returnIntLastDayOfMonth(currentDate);

        double totalAmount = 0;

        //find all previous payments of the current month
        for (Income income : mIncomes) {
            incomeDateAmountPairs.addAll(income.getAllMonthlyIncomes());
        }

        for (int i = 0; i < monthLength ; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i+1);
            Log.d(TAG, "Testing date: " + DateFormatter.returnSimpleDateTime(calendar.getTime()));

            for (DateAmountPair incomePair : incomeDateAmountPairs) {
                if ( DateFormatter.setTimeToMidnight(incomePair.getDate()).equals(calendar.getTime()) ) {
                    totalAmount += incomePair.getAmount();
                }
            }
        }

        return totalAmount;
    }

    public double getTotalExpense() {
        double expenseTotal = 0;

        for (Expense e: mExpenses) {
            expenseTotal += e.getExpenseCost();
        }

        Log.d(TAG, "Total expense = " + expenseTotal);
        return expenseTotal;
    }

    public double getCurrentMonthExpense() {
        ArrayList<DateAmountPair> expenseDateAmountPairs = new ArrayList<>();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateFormatter.setTimeToMidnight(currentDate));
        int monthLength = DateFormatter.returnIntLastDayOfMonth(currentDate);

        double totalAmount = 0;

        for (Expense expense : mExpenses) {
            expenseDateAmountPairs.addAll(expense.getAllMonthlyExpenses());
        }

        for (int i = 0; i < monthLength ; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i+1);
            Log.d(TAG, "Testing date: " + DateFormatter.returnSimpleDateTime(calendar.getTime()));

            for (DateAmountPair expensePair : expenseDateAmountPairs) {
                if ( DateFormatter.setTimeToMidnight(expensePair.getDate()).equals(calendar.getTime()) ) {
                    totalAmount += expensePair.getAmount();
                    Log.d(TAG, "Subtracting amount: " + expensePair.getAmount() + " for date " + expensePair.getDate());
                }
            }
        }

        return totalAmount;
    }

    public static Budget get(Context c) {
        if (sBudget == null) {
            return sBudget = new Budget(c.getApplicationContext());
        }

        return sBudget;
    }

    public ArrayList<Income> getIncomes() {
        return BudgetDatabase.getIncomes();
        //return mIncomes;
    }
    public ArrayList<Expense> getExpenses() {
        return BudgetDatabase.getExpenses();
        //return mExpenses;
    }


    public Income getIncome(UUID incomeId) {
        return BudgetDatabase.getIncome(incomeId);
        /*
        for(Income i : mIncomes) {
            if(i.getIncomeId().equals(incomeId)) {
                return i;
            }
        }
        return null;
        */
    }

    public void deleteIncome(Income income) {
        //mIncomes.remove(income);
        BudgetDatabase.deleteItem(income.getIncomeId());
    }

    public void addIncome (Income income) {
        //mIncomes.add(income);
        BudgetDatabase.addNewItem(income.getIncomeId(), income.getIncomeCollectionDate(), income.getIncomeFrequencyInt(), false);
    }

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

    public void saveIncome(Income income) {
        BudgetDatabase.updateItem(income.getIncomeId(),
                income.getIncomeTitle(),
                income.getIncomeAmount(),
                income.getIncomeCollectionDate(),
                income.getIncomeFrequency());
    }

    public Expense getExpense (UUID expenseId) {
        return BudgetDatabase.getExpense(expenseId);
        /*
        for(Expense e: mExpenses) {
            if(e.getExpenseId().equals(expenseId)) {
                return e;
            }
        }
        return null;
        */
    }

    public void deleteExpense (Expense expense) {
       //mExpenses.remove(expense);
        BudgetDatabase.deleteItem(expense.getExpenseId());
    }

    public void addExpense (Expense expense) {
        //mExpenses.add(expense)
        BudgetDatabase.addNewItem(expense.getExpenseId(), expense.getExpensePayDate(), expense.getExpenseFrequencyInt(), true);
    }

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

    public void saveExpense(Expense expense) {
        BudgetDatabase.updateItem(expense.getExpenseId(),
                expense.getExpenseTitle(),
                expense.getExpenseCost(),
                expense.getExpensePayDate(),
                expense.getExpenseFrequency());
    }

    public void openDatabase() {
        BudgetDatabase.createDatabase(mAppContext);
    }

    public void loadBudgetToDatabase() {
        BudgetDatabase.createDatabase(mAppContext);
        BudgetDatabase.cleanDatabase();
        for (Expense e : mExpenses ) {
            BudgetDatabase.addExpense(e.getExpenseId(), e.getExpenseTitle(), e.getExpenseCost(), e.getNextExpensePayDate(e.getExpenseFrequency(), new Date()), e.getExpenseFrequency());
        }

        for (Income i : mIncomes) {
            BudgetDatabase.addIncome(i.getIncomeId(), i.getIncomeTitle(), i.getIncomeAmount(), i.getNextIncomeCollectionDate(i.getIncomeFrequency(), new Date()), i.getIncomeFrequency());
        }


    }

    public void logBudgetFromDatabase() {
        BudgetDatabase.logCurrentBudget();
    }
}
