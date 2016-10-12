package io.github.plastix.prolificlibrary.ui.add;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import io.github.plastix.prolificlibrary.ui.base.ActivityModule;

@Module
public class AddModule extends ActivityModule {
    public AddModule(AppCompatActivity activity) {
        super(activity);
    }
}
