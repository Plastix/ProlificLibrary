package io.github.plastix.prolificlibrary.ui.list;

import android.util.Log;
import android.view.View;

import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.ui.base.AbstractViewModel;

public class BookViewModel extends AbstractViewModel {

    private Book book;

    public BookViewModel(Book book) {
        this.book = book;
    }

    public String getTitle() {
        return book.title;
    }

    public String getAuthor() {
        return book.author;
    }

    public View.OnClickListener onClick() {
        return v -> Log.d("BookViewModel", "book clicked!");
    }
}
