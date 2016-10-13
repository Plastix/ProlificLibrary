package io.github.github.plastix.prolificlibrary.ui.detail;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.ui.detail.DetailViewModel;

public class DetailViewModelTest {

    Book book;

    DetailViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        book = new Book();
        book.title = "Some Title";
        book.author = "Some Author";
        book.publisher = "Some Publisher";
        book.categories = "Category 1, Category 2";
        book.checkedOutAuthor = "Some Person";


        viewModel = new DetailViewModel(book);
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
    public void getPublisher_returnsCorrectPublisher() {
        Assert.assertEquals(book.publisher, viewModel.getPublisher());
    }

    @Test
    public void getCategories_returnsCorrectCategoryString() {
        Assert.assertEquals(book.categories, viewModel.getCategories());
    }
}
