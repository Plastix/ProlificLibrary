package io.github.plastix.prolificlibrary.ui.detail;

import android.util.Log;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.ui.ActivityScope;
import io.github.plastix.prolificlibrary.ui.base.RxViewModel;

public class DetailViewModel extends RxViewModel {

    private Book book;

    @Inject
    public DetailViewModel(@ActivityScope Book book) {
        this.book = book;
    }

    public void onFabClick() {
        Log.d("DetailViewModel", "Share clicked!");
    }

    public void onCheckoutClick() {
        Log.d("DetailViewModel", "Checkout clicked!");
    }

    public String getTitle() {
        return book.title;
    }

    public String getAuthor() {
        return book.author;
    }

    public String getPublisher() {
        return book.publisher;
    }

    public String getCategories() {
        return book.categories;
    }

    public String getCheckedOut() {
        return book.checkedOutAuthor;
    }


}
