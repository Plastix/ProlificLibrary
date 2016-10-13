package io.github.plastix.prolificlibrary.ui.add;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import com.jakewharton.rxrelay.PublishRelay;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.data.remote.LibraryService;
import io.github.plastix.prolificlibrary.ui.base.RxViewModel;
import io.github.plastix.prolificlibrary.util.RxUtils;
import io.github.plastix.prolificlibrary.util.StringUtils;
import rx.Completable;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class AddViewModel extends RxViewModel {

    private final ObservableBoolean submitEnabled = new ObservableBoolean(false);
    private final ObservableInt progressVisibility = new ObservableInt(View.GONE);
    private final ObservableField<String> bookTitle = new ObservableField<>();
    private final ObservableField<String> bookAuthor = new ObservableField<>();
    private final ObservableField<String> bookPublisher = new ObservableField<>();
    private final ObservableField<String> bookCategories = new ObservableField<>();

    private final PublishRelay<Throwable> networkError = PublishRelay.create();
    private final PublishSubject submitStatus = PublishSubject.create();

    private LibraryService libraryService;

    @Inject
    public AddViewModel(LibraryService libraryService) {
        this.libraryService = libraryService;
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
    }

    public void submit() {
        Subscription subscription = libraryService.submitBook(bookTitle.get(),
                bookCategories.get(), bookTitle.get(), bookPublisher.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    progressVisibility.set(View.VISIBLE);
                    submitEnabled.set(false);
                })
                .subscribe(book -> {
                    progressVisibility.set(View.GONE);
                    submitStatus.onCompleted();
                }, throwable -> {
                    progressVisibility.set(View.GONE);
                    submitEnabled.set(true);
                    networkError.call(throwable);
                });

        unsubscribeOnDestroy(subscription);
    }

    public Completable onSubmitSuccess() {
        // Successful submissions only happen once per screen. Expose this as a completable
        return submitStatus.toCompletable();
    }

    public boolean hasData() {
        return StringUtils.isNotNullOrEmpty(bookTitle.get()) ||
                StringUtils.isNotNullOrEmpty(bookAuthor.get()) ||
                StringUtils.isNotNullOrEmpty(bookPublisher.get()) ||
                StringUtils.isNotNullOrEmpty(bookCategories.get());
    }

    public Observable<Throwable> networkErrors() {
        return networkError.asObservable();
    }
}
