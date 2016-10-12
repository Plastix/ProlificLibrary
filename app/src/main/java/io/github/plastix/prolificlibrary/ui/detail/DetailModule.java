package io.github.plastix.prolificlibrary.ui.detail;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.ui.ActivityScope;
import io.github.plastix.prolificlibrary.ui.base.ActivityModule;

@Module
public class DetailModule extends ActivityModule {

    private Book book;

    public DetailModule(AppCompatActivity activity, Book book) {
        super(activity);
        this.book = book;
    }

    @ActivityScope
    @Provides
    public Book provideBook() {
        return book;
    }


}
