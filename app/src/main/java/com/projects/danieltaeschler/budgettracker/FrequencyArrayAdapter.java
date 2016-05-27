package com.projects.danieltaeschler.budgettracker;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Daniel Taeschler on 5/18/2016.
 */
public class FrequencyArrayAdapter extends ArrayAdapter<Frequency> {

    private Context mContext;
    private TextView mSpinnerText;

    public FrequencyArrayAdapter(Context context, ArrayList<Frequency> frequencies) {
        super(context, 0, frequencies);
        mContext = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.spinner_frequency, parent, false);
        }

        Frequency f = getItem(position);

        mSpinnerText = (TextView) view.findViewById(R.id.spinner_frequency_text);
        mSpinnerText.setText(f.toString());

        return view;

    }

    @TargetApi(17)
    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        TextView spinnerText = new TextView(mContext);
        spinnerText.setText(getItem(position).toString());
        spinnerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        spinnerText.setTextColor(Color.BLACK);
        spinnerText.setTextSize(18);
        return spinnerText;
    }
}
