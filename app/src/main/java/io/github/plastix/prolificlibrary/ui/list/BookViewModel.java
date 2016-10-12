package io.github.plastix.prolificlibrary.ui.list;

import android.view.View;

import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.ui.base.AbstractViewModel;
import io.github.plastix.prolificlibrary.ui.detail.DetailActivity;

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
        return v -> context.startActivity(DetailActivity.newIntent(context, book));
    }
}
