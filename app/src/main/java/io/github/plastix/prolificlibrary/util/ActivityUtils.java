package io.github.plastix.prolificlibrary.util;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class ActivityUtils {

    public static void setBackEnabled(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
