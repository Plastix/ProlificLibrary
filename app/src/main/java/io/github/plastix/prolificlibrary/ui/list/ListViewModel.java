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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListViewModel extends RxViewModel {

    private final ObservableInt emptyViewVisibility = new ObservableInt();
    private BehaviorRelay<List<Book>> booksRelay = BehaviorRelay.create();
    private PublishRelay<Void> fabClicks = PublishRelay.create();

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
        Subscription sub = libraryService.fetchAllBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateBooks, Throwable::printStackTrace);
        // TODO Handle errors gracefully

        unsubscribeOnDestroy(sub);
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

    public ObservableInt getEmptyVisibility() {
        return emptyViewVisibility;
    }

}
