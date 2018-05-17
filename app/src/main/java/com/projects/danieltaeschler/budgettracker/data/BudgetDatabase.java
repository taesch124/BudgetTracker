package com.projects.danieltaeschler.budgettracker.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.projects.danieltaeschler.budgettracker.Frequency;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense.Expense;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income.Income;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Daniel Taeschler on 4/7/2018.
 */

public class BudgetDatabase {

    private static SQLiteDatabase mBudgetDatabase;

    public static void createDatabase(Context context) {
        try {
            mBudgetDatabase = context.openOrCreateDatabase("BudgetTracker", Context.MODE_PRIVATE, null);
            mBudgetDatabase.execSQL("CREATE TABLE IF NOT EXISTS expense_income (" +
                    "id VARCHAR PRIMARYKEY, " +
                    "title VARCHAR, " +
                    "cost REAL, " +
                    "next_pay_date REAL, " +
                    "frequency INT, " +
                    "expense_flag INT)");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanDatabase() { mBudgetDatabase.execSQL("DELETE FROM expense_income WHERE id = '9cac3fea-c616-4570-a2b7-a3f31097a85f'");}

    public static void addNewItem(UUID id, Date date, int frequency, boolean expenseFlag) {
        int expenseFlagInt = expenseFlag ? 1 : 0;
        mBudgetDatabase.execSQL("INSERT INTO expense_income (id, next_pay_date, frequency, expense_flag) " +
                                "VALUES ('" + id.toString() + "',  " + date.getTime() + ", " + frequency + ", " + expenseFlagInt + ")");
    }

    public static void addExpense(UUID id, String title, double cost, Date nextPayDate, Frequency frequency) {
        mBudgetDatabase.execSQL("INSERT INTO expense_income (id, title, cost, next_pay_date, frequency, expense_flag) " +
                "VALUES ('" + id.toString() + "', '" + title + "', " + cost + ", " + nextPayDate.getTime() + ", " + frequency.getYearlyFrequency() + ", 1)");
    }

    public static void addIncome(UUID id, String title, double cost, Date nextPayDate, Frequency frequency) {
        mBudgetDatabase.execSQL("INSERT INTO expense_income (id, title, cost, next_pay_date, frequency, expense_flag) " +
                "VALUES ('" + id.toString() + "', '" + title + "', " + cost + ", " + nextPayDate.getTime() + ", " + frequency.getYearlyFrequency() + ", 0)");
    }


    public static void updateItem(UUID id, String title, double cost, Date nextPayDate, Frequency frequency) {
        mBudgetDatabase.execSQL("UPDATE expense_income " +
                        "SET title = '" + title + "', " +
                        "cost = " + cost + ", " +
                        "next_pay_date = " + nextPayDate.getTime() + ", " +
                        "frequency = " + frequency.getYearlyFrequency() + " " +
                        "WHERE id = '" + id.toString() + "'");

    }

    public static void deleteItem(UUID id) {
        mBudgetDatabase.execSQL("DELETE FROM expense_income WHERE id = '" + id.toString() + "'");
    }

    public static ArrayList<Income> getIncomes() {
        ArrayList<Income> incomes = new ArrayList<>();

        Cursor c = mBudgetDatabase.rawQuery("SELECT id, title, cost, next_pay_date, frequency FROM expense_income WHERE expense_flag = 0", null);
        int idIndex = c.getColumnIndex("id");
        int titleIndex = c.getColumnIndex("title");
        int costIndex = c.getColumnIndex("cost");
        int payDateIndex = c.getColumnIndex("next_pay_date");
        int frequencyIndex = c.getColumnIndex("frequency");

        while(c.moveToNext()) {
            Income i = new Income(c.getString(idIndex),
                    c.getString(titleIndex),
                    c.getDouble(costIndex),
                    new Date(c.getLong(payDateIndex)),
                    c.getInt(frequencyIndex));
            i.setIncomeCollectionDate(i.getNextIncomeCollectionDate(i.getIncomeFrequency(), i.getIncomeCollectionDate()));
            incomes.add(i);
        }

        return incomes;
    }

    public static Income getIncome(UUID id) {
        Cursor c = mBudgetDatabase.rawQuery("SELECT id, title, cost, next_pay_date, frequency FROM expense_income WHERE id = '" + id.toString() + "'", null);
        int idIndex = c.getColumnIndex("id");
        int titleIndex = c.getColumnIndex("title");
        int costIndex = c.getColumnIndex("cost");
        int payDateIndex = c.getColumnIndex("next_pay_date");
        int frequencyIndex = c.getColumnIndex("frequency");
        c.moveToFirst();
        Income i = new Income(c.getString(idIndex),
                c.getString(titleIndex),
                c.getDouble(costIndex),
                new Date(c.getLong(payDateIndex)),
                c.getInt(frequencyIndex));
        i.setIncomeCollectionDate(i.getNextIncomeCollectionDate(i.getIncomeFrequency(), i.getIncomeCollectionDate()));
        return i;
    }

    public static ArrayList<Expense> getExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();

        Cursor c = mBudgetDatabase.rawQuery("SELECT id, title, cost, next_pay_date, frequency FROM expense_income WHERE expense_flag = 1", null);
        int idIndex = c.getColumnIndex("id");
        int titleIndex = c.getColumnIndex("title");
        int costIndex = c.getColumnIndex("cost");
        int payDateIndex = c.getColumnIndex("next_pay_date");
        int frequencyIndex = c.getColumnIndex("frequency");

        while(c.moveToNext()) {
            Expense e = new Expense(c.getString(idIndex),
                    c.getString(titleIndex),
                    c.getDouble(costIndex),
                    new Date(c.getLong(payDateIndex)),
                    c.getInt(frequencyIndex));
            e.setExpensePayDate(e.getNextExpensePayDate(e.getExpenseFrequency(), e.getExpensePayDate()));
            expenses.add(e);
        }

        return expenses;
    }

    public static Expense getExpense(UUID id) {
        Cursor c = mBudgetDatabase.rawQuery("SELECT id, title, cost, next_pay_date, frequency FROM expense_income WHERE id = '" + id.toString() + "'", null);
        int idIndex = c.getColumnIndex("id");
        int titleIndex = c.getColumnIndex("title");
        int costIndex = c.getColumnIndex("cost");
        int payDateIndex = c.getColumnIndex("next_pay_date");
        int frequencyIndex = c.getColumnIndex("frequency");
        c.moveToFirst();
        Expense e = new Expense(c.getString(idIndex),
                c.getString(titleIndex),
                c.getDouble(costIndex),
                new Date(c.getLong(payDateIndex)),
                c.getInt(frequencyIndex));
        e.setExpensePayDate(e.getNextExpensePayDate(e.getExpenseFrequency(), e.getExpensePayDate()));
        return e;
    }

    public static void logCurrentBudget() {
        Log.i("DATABASE", "Logging current expenses and incomes.");
        Cursor c = mBudgetDatabase.rawQuery("SELECT * FROM expense_income", null);
        int idIndex = c.getColumnIndex("id");
        int titleIndex = c.getColumnIndex("title");
        int costIndex = c.getColumnIndex("cost");
        int payDateIndex = c.getColumnIndex("next_pay_date");
        int frequencyIndex = c.getColumnIndex("frequency");
        int expenseFlagIndex = c.getColumnIndex("expense_flag");

        //Date payDate = new Date();
        //payDate =

        while (c.moveToNext()) {
            Log.i("EXPENSE_INCOME", "item - " + c.getString(idIndex) + ": " + c.getString(titleIndex) + "\n" +
                    "$" + c.getDouble(costIndex) + ", " + c.getInt(frequencyIndex) + " next payment " + c.getLong(payDateIndex) +
                    "\n expense? " + c.getInt(expenseFlagIndex));
        }
    }
}
