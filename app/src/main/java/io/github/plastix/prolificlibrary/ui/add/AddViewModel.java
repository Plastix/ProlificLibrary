package io.github.plastix.prolificlibrary.ui.add;

import android.content.res.Resources;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.Nullable;
import android.view.View;

import com.jakewharton.rxrelay.PublishRelay;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.data.remote.LibraryService;
import io.github.plastix.prolificlibrary.ui.ActivityScope;
import io.github.plastix.prolificlibrary.ui.base.RxViewModel;
import io.github.plastix.prolificlibrary.util.RxUtils;
import io.github.plastix.prolificlibrary.util.StringUtils;
import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddViewModel extends RxViewModel {

    private final ObservableBoolean submitEnabled = new ObservableBoolean(false);
    private final ObservableInt progressVisibility = new ObservableInt(View.GONE);
    private final ObservableField<String> bookTitle = new ObservableField<>();
    private final ObservableField<String> bookAuthor = new ObservableField<>();
    private final ObservableField<String> bookPublisher = new ObservableField<>();
    private final ObservableField<String> bookCategories = new ObservableField<>();

    private final PublishRelay<Throwable> networkError = PublishRelay.create();
    private final PublishRelay<Book> submitStatus = PublishRelay.create();

    private final LibraryService libraryService;
    private final Resources resources;
    private final Book book;

    @Inject
    public AddViewModel(LibraryService libraryService,
                        Resources resources,
                        @ActivityScope @Nullable Book book) {
        this.libraryService = libraryService;
        this.resources = resources;
        this.book = book;
    }

    public ObservableBoolean getSubmitEnabled() {
        return submitEnabled;
    }

    public ObservableField<String> getBookTitle() {
        return bookTitle;
    }

    public ObservableField<String> getBookAuthor() {
        return bookAuthor;
    }

    public ObservableField<String> getBookPublisher() {
        return bookPublisher;
    }

    public ObservableField<String> getBookCategories() {
        return bookCategories;
    }

    public ObservableInt getProgressVisibility() {
        return progressVisibility;
    }

    @Override
    public void bind() {
        unsubscribeOnUnbind(
                Observable.combineLatest(RxUtils.toObservable(bookTitle),
                        RxUtils.toObservable(bookAuthor),
                        RxUtils.toObservable(bookPublisher),
                        RxUtils.toObservable(bookCategories),
                        (s, s2, s3, s4) -> StringUtils.isNotNullOrEmpty(s) &&
                                StringUtils.isNotNullOrEmpty(s2) &&
                                StringUtils.isNotNullOrEmpty(s3) &&
                                StringUtils.isNotNullOrEmpty(s4)
                ).subscribe(submitEnabled::set)
        );

        if (isEditingBook()) {
            populateFields(book);
        }
    }

    private boolean isEditingBook() {
        return book != null;
    }

    private void populateFields(Book book) {
        if (book != null) {
            bookTitle.set(book.title);
            bookAuthor.set(book.author);
            bookCategories.set(book.categories);
            bookPublisher.set(book.publisher);
        }
    }

    public void submit() {
        Subscription subscription = getApiCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    progressVisibility.set(View.VISIBLE);
                    submitEnabled.set(false);
                })
                .subscribe(book -> {
                    progressVisibility.set(View.GONE);
                    submitStatus.call(book);
                }, throwable -> {
                    progressVisibility.set(View.GONE);
                    submitEnabled.set(true);
                    networkError.call(throwable);
                });

        unsubscribeOnDestroy(subscription);
    }

    // Switches the API call between adding or updating a book depending on if the viewModel has
    // a copy of a book model
    private Single<Book> getApiCall() {
        if (!isEditingBook()) {
            return libraryService.submitBook(bookAuthor.get(),
                    bookCategories.get(), bookTitle.get(), bookPublisher.get());
        } else {
            return libraryService.updateBook(String.valueOf(book.id), bookAuthor.get(),
                    bookCategories.get(), bookTitle.get(), bookPublisher.get(), null);
        }
    }

    public Observable<Book> onSubmitSuccess() {
        return submitStatus;
    }

    public boolean hasData() {
        return StringUtils.isNotNullOrEmpty(bookTitle.get()) ||
                StringUtils.isNotNullOrEmpty(bookAuthor.get()) ||
                StringUtils.isNotNullOrEmpty(bookPublisher.get()) ||
                StringUtils.isNotNullOrEmpty(bookCategories.get());
    }

    public String getScreenTitle() {
        if (isEditingBook()) {
            return resources.getString(R.string.add_screen_title_edit);
        } else {
            return resources.getString(R.string.add_screen_title);
        }
    }

    public Observable<Throwable> networkErrors() {
        return networkError.asObservable();
    }
}
