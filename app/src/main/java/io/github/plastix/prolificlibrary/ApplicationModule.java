package io.github.plastix.prolificlibrary;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final LibraryApp app;

    public ApplicationModule(LibraryApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public static Resources provideResources(@ApplicationQualifier Context context) {
        return context.getResources();

    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    @ApplicationQualifier
    public Context provideApplicationContext() {
        return app.getApplicationContext();
    }
}
