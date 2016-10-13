package io.github.github.plastix.prolificlibrary.ui.list;

import android.content.Context;
import android.content.Intent;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.ui.list.BookViewModel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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

        viewModel = new BookViewModel(book, context);
        viewModel.bind();
    }

    @Test
    public void getTitle_returnsCorrectTitle() {
        Assert.assertEquals(book.title, viewModel.getTitle());
    }

    @Test
    public void getAuthor_returnsCorrectAuthor() {
        Assert.assertEquals(book.author, viewModel.getAuthor());
    }

    @Test
    public void onClick_shouldOpenActivity() {
        viewModel.onClick();

        verify(context).startActivity(any(Intent.class));
    }
}
