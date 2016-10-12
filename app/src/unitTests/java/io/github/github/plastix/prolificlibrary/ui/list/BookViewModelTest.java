package io.github.github.plastix.prolificlibrary.ui.list;

import android.content.Context;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.ui.list.BookViewModel;

public class BookViewModelTest {


    @Mock
    Context context;

    Book book;

    BookViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        book = new Book();
        book.title = "Some Title";
        book.author = "Some Author";

        viewModel = new BookViewModel(book);
        viewModel.bind(context);
    }

    @Test
    public void getTitle_returnsCorrectTitle() {
        Assert.assertEquals(book.title, viewModel.getTitle());
    }

    @Test
    public void getAuthor_returnsCorrectAuthor() {
        Assert.assertEquals(book.author, viewModel.getAuthor());
    }
}
