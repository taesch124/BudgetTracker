package com.projects.danieltaeschler.budgettracker.utilities;

import android.content.Context;
import android.util.Log;

import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.expense.Expense;
import com.projects.danieltaeschler.budgettracker.com.projects.danieltaeschler.budgettracker.income.Income;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Daniel Taeschler on 5/11/2016.
 */
public class BudgetTrackerJSONSerializer {
    private static final String TAG = "JSONSerializer";

    private Context mContext;
    private String mExpenseFilename;
    private String mIncomeFilename;

    public BudgetTrackerJSONSerializer(Context c, String expenseFilename, String incomeFilename) {
        mContext = c;
        mExpenseFilename = expenseFilename;
        mIncomeFilename = incomeFilename;
    }

    public void saveIncomes (ArrayList<Income> incomes) throws JSONException, IOException {
        //Build an array in JSON
        JSONArray array = new JSONArray();

        for (Income i : incomes) {
            array.put(i.toJSON());
        }

        //Write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mIncomeFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Income> loadIncomes() throws IOException, JSONException {
        ArrayList<Income> incomes = new ArrayList<>();
        BufferedReader reader = null;

        try {
            //Open and read the stream into a string builder
            InputStream in = mContext.openFileInput(mIncomeFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //Line breaks are omitted and irrelevant
                jsonString.append(line);
            }

            //Parse the json using JSONTokenizer
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            //Build the array of crimes from JSON Objects
            for (int i=0; i < array.length(); i++) {
                incomes.add(new Income(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            //Ignore, this happens when starting with no Expenses
        } catch (Exception e) {
            Log.e(TAG, "Error loading incomes.", e);
        }finally
        {
            if (reader != null) {
                reader.close();
            }
        }

        return incomes;
    }

    public void saveExpenses (ArrayList<Expense> expenses) throws JSONException, IOException {
        //Build an array in JSON
        JSONArray array = new JSONArray();

        for (Expense e : expenses) {
            array.put(e.toJSON());
        }

        //Write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mExpenseFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    public ArrayList<Expense> loadExpenses() throws IOException, JSONException {
        ArrayList<Expense> expenses = new ArrayList<>();
        BufferedReader reader = null;

        try {
            //Open and read the stream into a string builder
            InputStream in = mContext.openFileInput(mExpenseFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //Line breaks are omitted and irrelevant
                jsonString.append(line);
            }

            //Parse the json using JSONTokenizer
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            //Build the array of crimes from JSON Objects
            for (int i=0; i < array.length(); i++) {
                expenses.add(new Expense(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            //Ignore, this happens when starting with no Expenses
        } catch(Exception e ) {
            Log.e(TAG, "Error loading expenses.", e);
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }

        return expenses;
    }
}
