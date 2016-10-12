package io.github.plastix.prolificlibrary.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.plastix.prolificlibrary.ApplicationComponent;
import io.github.plastix.prolificlibrary.LibraryApp;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies(LibraryApp.getComponent(this));
    }

    protected abstract void injectDependencies(ApplicationComponent component);

}
