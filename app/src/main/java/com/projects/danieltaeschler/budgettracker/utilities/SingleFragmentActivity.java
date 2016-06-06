package com.projects.danieltaeschler.budgettracker.utilities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import com.projects.danieltaeschler.budgettracker.R;

/**
 * Created by Daniel Taeschler on 12/9/2015.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    //abstract class to be implemented by the fragments to return the fragment needed
    // //for their specific case
    protected abstract Fragment createFragment();

    //Use fragment manager to attach fragment from onCreate() to this activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
