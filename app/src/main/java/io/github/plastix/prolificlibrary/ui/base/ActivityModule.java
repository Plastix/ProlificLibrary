package io.github.plastix.prolificlibrary.ui.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.prolificlibrary.ui.ActivityScope;

@Module
public class ActivityModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    public Context provideActivityContext() {
        return activity.getBaseContext();
    }

}
