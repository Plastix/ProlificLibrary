package io.github.plastix.prolificlibrary.ui.list;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.prolificlibrary.ui.ActivityScope;
import io.github.plastix.prolificlibrary.ui.base.ActivityModule;

@Module
public class ListModule extends ActivityModule {

    public ListModule(AppCompatActivity activity) {
        super(activity);
    }

    @Provides
    @ActivityScope
    public static LinearLayoutManager provideLinearLayoutManager(@ActivityScope Context context) {
        return new LinearLayoutManager(context);
    }

    @Provides
    @ActivityScope
    public static BookAdapter provideBookAdapter(@ActivityScope Context context) {
        return new BookAdapter(context);
    }
}
