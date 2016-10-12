package io.github.plastix.prolificlibrary.ui.list;

import android.databinding.ObservableInt;
import android.view.View;

import com.jakewharton.rxrelay.BehaviorRelay;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.data.remote.LibraryService;
import io.github.plastix.prolificlibrary.ui.add.AddActivity;
import io.github.plastix.prolificlibrary.ui.base.RxViewModel;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListViewModel extends RxViewModel {

    private final ObservableInt emptyViewVisibility;
    private LibraryService libraryService;
    private BehaviorRelay<List<Book>> booksRelay;

    @Inject
    public ListViewModel(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.booksRelay = BehaviorRelay.create();
        this.emptyViewVisibility = new ObservableInt();
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
        context.startActivity(AddActivity.newIntent(context));
    }

    public Observable<List<Book>> getBooks() {
        return booksRelay.asObservable();
    }

    public ObservableInt getEmptyVisibility() {
        return emptyViewVisibility;
    }

}
