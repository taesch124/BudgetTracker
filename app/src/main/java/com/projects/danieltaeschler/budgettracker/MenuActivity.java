package com.projects.danieltaeschler.budgettracker;

import android.support.v4.app.Fragment;

/**
 * Created by Daniel Taeschler on 12/18/2015.
 */
public class MenuActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new MenuFragment();
    }
}
