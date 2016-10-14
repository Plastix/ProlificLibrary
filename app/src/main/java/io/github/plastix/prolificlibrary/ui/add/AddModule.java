package io.github.plastix.prolificlibrary.ui.add;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.ui.ActivityScope;
import io.github.plastix.prolificlibrary.ui.base.ActivityModule;

@Module
public class AddModule extends ActivityModule {

    private final Book book;

    public AddModule(AppCompatActivity activity, Book book) {
        super(activity);
        this.book = book;
    }

    @Provides
    @ActivityScope
    @Nullable
    public Book getBook() {
        return book;
    }
}
