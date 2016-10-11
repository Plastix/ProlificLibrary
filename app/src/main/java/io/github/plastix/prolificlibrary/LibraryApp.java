package io.github.plastix.prolificlibrary;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

public class LibraryApp extends Application {

    private ApplicationComponent applicationComponent;

    public static ApplicationComponent getComponent(@NonNull Context context) {
        return ((LibraryApp) context.getApplicationContext()).getApplicationComponent();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.applicationComponent = prepareApplicationComponent().build();
    }

    private DaggerApplicationComponent.Builder prepareApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this));
    }
}
