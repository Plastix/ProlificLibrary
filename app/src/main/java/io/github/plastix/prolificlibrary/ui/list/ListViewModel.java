package io.github.plastix.prolificlibrary.ui.list;

import android.databinding.ObservableInt;
import android.view.View;

import com.jakewharton.rxrelay.BehaviorRelay;
import com.jakewharton.rxrelay.PublishRelay;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.data.remote.LibraryService;
import io.github.plastix.prolificlibrary.ui.base.RxViewModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListViewModel extends RxViewModel {

    private final ObservableInt emptyViewVisibility = new ObservableInt();
    private BehaviorRelay<List<Book>> booksRelay = BehaviorRelay.create();
    private PublishRelay<Void> fabClicks = PublishRelay.create();
    private PublishRelay<Throwable> fetchErrors = PublishRelay.create();
    private PublishRelay<Throwable> deleteErrors = PublishRelay.create();

    private LibraryService libraryService;

    @Inject
    public ListViewModel(LibraryService libraryService) {
        this.libraryService = libraryService;
        updateBooks(new ArrayList<>());
    }

    private void updateBooks(List<Book> books) {
        booksRelay.call(books);

        if (books.size() == 0) {
            emptyViewVisibility.set(View.VISIBLE);
        } else {
            emptyViewVisibility.set(View.GONE);
        }
    }

    public void fetchBooks() {
        unsubscribeOnDestroy(libraryService.fetchAllBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateBooks,
                        throwable -> fetchErrors.call(throwable)));

    }

    public void deleteBooks() {
        unsubscribeOnDestroy(libraryService.clearAllBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Void -> this.updateBooks(new ArrayList<>()),
                        throwable -> deleteErrors.call(throwable)));

    }

    public void onFabClick() {
        fabClicks.call(null);
    }

    public Observable<Void> fabClicks() {
        return fabClicks;
    }

    public Observable<List<Book>> getBooks() {
        return booksRelay.asObservable();
    }

    public Observable<Throwable> fetchErrors() {
        return fetchErrors;
    }

    public Observable<Throwable> deleteErrors() {
        return deleteErrors;
    }

    public ObservableInt getEmptyVisibility() {
        return emptyViewVisibility;
    }


}
