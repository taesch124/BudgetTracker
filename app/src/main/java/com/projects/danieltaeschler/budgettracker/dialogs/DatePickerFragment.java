package com.projects.danieltaeschler.budgettracker.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.projects.danieltaeschler.budgettracker.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Daniel Taeschler on 5/21/2016.
 */
public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.projects.danieltaeschler.budgettracker.date";

    private Date mDate;

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();

        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);

        //Create a calendar to get the year, month, and day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //apply layout to dialog
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);

        //Initialize the datePicker with the date from the Intent using year, month, day variables
        DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Translate the year, month, and day into Date object using the calendar
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();

                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_dialog_label)
                //When the user clicks OK, send data back to calling Fragment to update model
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    public void sendResult(int resultCode) {
        if(getTargetFragment() == null) {
            return;
        }

        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
