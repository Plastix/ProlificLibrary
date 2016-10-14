package io.github.plastix.prolificlibrary.ui.list;

import android.content.Context;

import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.ui.base.AbstractViewModel;
import io.github.plastix.prolificlibrary.ui.detail.DetailActivity;

public class BookViewModel extends AbstractViewModel {

    private final Book book;
    private Context context;

    public BookViewModel(Book book, Context context) {
        this.book = book;
        this.context = context;
    }

    public void onClick() {
        context.startActivity(DetailActivity.newIntent(context, book));
    }

    public String getTitle() {
        return book.title;
    }

    public String getAuthor() {
        return book.author;
    }

    @Override
    public void unbind() {
        super.unbind();
        this.context = null;
    }
}
