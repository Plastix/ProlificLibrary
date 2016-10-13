package io.github.plastix.prolificlibrary.ui.detail;

import android.content.res.Resources;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.view.View;

import com.jakewharton.rxrelay.PublishRelay;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.data.remote.LibraryService;
import io.github.plastix.prolificlibrary.ui.ActivityScope;
import io.github.plastix.prolificlibrary.ui.base.RxViewModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailViewModel extends RxViewModel {

    private final SimpleDateFormat humanReadableDate = new SimpleDateFormat("MMMM d, y h:ma", Locale.getDefault());

    private Book book;
    private LibraryService libraryService;
    private Resources resources;

    private ObservableBoolean checkoutEnabled = new ObservableBoolean(true);
    private ObservableInt loadingVisibility = new ObservableInt(View.GONE);
    private PublishRelay<Void> checkoutClicks = PublishRelay.create();
    private PublishRelay<Book> checkOuts = PublishRelay.create();
    private PublishRelay<Throwable> checkoutErrors = PublishRelay.create();

    @Inject
    public DetailViewModel(@ActivityScope Book book,
                           LibraryService libraryService,
                           Resources resources) {
        this.book = book;
        this.libraryService = libraryService;
        this.resources = resources;
    }

    public String getTitle() {
        return book.title;
    }

    public String getAuthor() {
        return book.author;
    }

    public String getPublisher() {
        String publisherString = (book.publisher != null) ? book.publisher :
                resources.getString(R.string.detail_no_publisher);

        return String.format(resources.getString(R.string.detail_publisher_text), publisherString);
    }

    public String getCategories() {
        String categoryString = (book.categories != null) ? book.categories :
                resources.getString(R.string.detail_no_category);

        return String.format(resources.getString(R.string.detail_categories_text), categoryString);
    }

    public String getCheckedOut() {
        String authorString = (book.checkedOutAuthor != null) ? book.checkedOutAuthor :
                resources.getString(R.string.detail_no_name);
        String dateString = (book.checkedOutDate != null) ? humanReadableDate.format(book.checkedOutDate) :
                resources.getString(R.string.detail_no_date);

        return String.format(resources.getString(R.string.detail_checkout_text),
                authorString, dateString);
    }

    public String getShareText() {
        return book.title;
    }

    public ObservableBoolean getCheckoutEnabled() {
        return checkoutEnabled;
    }

    public ObservableInt getLoadingVisibility() {
        return loadingVisibility;
    }

    public void click() {
        checkoutClicks.call(null);
    }

    public void checkout(String name) {
        libraryService.checkoutBook(book.id, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    loadingVisibility.set(View.VISIBLE);
                    checkoutEnabled.set(false);
                })
                .doOnEach(notification -> {
                    checkoutEnabled.set(true);
                    loadingVisibility.set(View.GONE);
                })
                .subscribe(checkOuts, checkoutErrors);
    }

    public Observable<Void> checkoutClicks() {
        return checkoutClicks;
    }

    public Observable<Book> successfulCheckOuts() {
        return checkOuts;
    }

    public Observable<Throwable> checkoutErrors() {
        return checkoutErrors;
    }


}
