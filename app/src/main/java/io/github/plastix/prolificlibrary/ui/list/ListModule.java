package io.github.plastix.prolificlibrary.ui.list;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import io.github.plastix.prolificlibrary.ui.base.ActivityModule;

@Module
public class ListModule extends ActivityModule {

    public ListModule(AppCompatActivity activity) {
        super(activity);
    }
}
