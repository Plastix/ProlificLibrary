package io.github.plastix.prolificlibrary.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;

import javax.inject.Named;

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
    @Named("SHARE_INTENT")
    public static Intent provideShareIntent(@ActivityScope AppCompatActivity activity, DetailViewModel detailViewModel) {
        Intent intent = ShareCompat.IntentBuilder.from(activity)
                .setText(detailViewModel.getShareText())
                .setType("text/plain")
                .getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @ActivityScope
    @Provides
    public static ShareActionProvider provideShareActionProvider(@ActivityScope Context context, @Named("SHARE_INTENT") Intent shareIntent) {
        ShareActionProvider shareActionProvider = new ShareActionProvider(context);
        shareActionProvider.setShareIntent(shareIntent);

        return shareActionProvider;
    }

    @ActivityScope
    @Provides
    public Book provideBook() {
        return book;
    }
}
