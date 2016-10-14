package io.github.github.plastix.prolificlibrary.ui.list;

import android.view.View;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.github.plastix.prolificlibrary.util.RxSchedulersOverrideRule;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.data.remote.LibraryService;
import io.github.plastix.prolificlibrary.ui.list.ListViewModel;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListViewModelTest {

    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock
    LibraryService libraryService;

    ListViewModel listViewModel;

    TestSubscriber<List<Book>> testSubscriber;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        listViewModel = new ListViewModel(libraryService);
        listViewModel.bind();

        testSubscriber = new TestSubscriber<>();
    }

    @Test
    public void getBooks_shouldReturnEmptyList() {
        listViewModel.getBooks().subscribe(testSubscriber);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(new ArrayList<>());
        testSubscriber.assertNoTerminalEvent();

    }

    @Test
    public void fetchBooks_shouldUpdateBooks() {
        List<Book> books = Arrays.asList(mock(Book.class), mock(Book.class), mock(Book.class));

        when(libraryService.fetchAllBooks()).thenReturn(
                Single.just(books));

        listViewModel.fetchBooks();
        listViewModel.getBooks().subscribe(testSubscriber);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(books);
        testSubscriber.assertNoTerminalEvent();
    }

    @Test
    public void fetchBooks_shouldError() {
        TestSubscriber<Throwable> testSubscriber = new TestSubscriber<>();
        Throwable error = new Throwable();
        when(libraryService.fetchAllBooks()).thenReturn(
                Single.error(error));

        listViewModel.fetchErrors().subscribe(testSubscriber);
        listViewModel.fetchBooks();

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(error);
    }

    @Test
    public void deleteBooks_shouldUpdateBooks() {
        TestSubscriber<List<Book>> testSubscriber = new TestSubscriber<>();
        when(libraryService.clearAllBooks()).thenReturn(
                Single.just(null));

        listViewModel.getBooks().subscribe(testSubscriber);
        listViewModel.deleteBooks();

        //noinspection unchecked
        testSubscriber.assertValues(new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void deleteBooks_shouldError() {
        TestSubscriber<Throwable> testSubscriber = new TestSubscriber<>();
        Throwable error = new Throwable();
        when(libraryService.clearAllBooks()).thenReturn(
                Single.error(error));

        listViewModel.deleteErrors().subscribe(testSubscriber);
        listViewModel.deleteBooks();

        testSubscriber.assertValue(error);
    }



    @Test
    public void emptyState_shouldBeVisibleByDefault() {
        Assert.assertEquals(listViewModel.getEmptyVisibility().get(), View.VISIBLE);
    }

    @Test
    public void emptyState_shouldBeGoneWhenBooksFetched() {
        List<Book> books = Arrays.asList(mock(Book.class), mock(Book.class), mock(Book.class));

        when(libraryService.fetchAllBooks()).thenReturn(
                Single.just(books));

        listViewModel.fetchBooks();

        Assert.assertEquals(listViewModel.getEmptyVisibility().get(), View.GONE);

    }

    @Test
    public void emptyState_shouldBeVisibleWhenEmptyList() {
        List<Book> books = new ArrayList<>();

        when(libraryService.fetchAllBooks()).thenReturn(
                Single.just(books));

        listViewModel.fetchBooks();

        Assert.assertEquals(listViewModel.getEmptyVisibility().get(), View.VISIBLE);

    }

    @Test
    public void fabClick_noCompletionByDefult() {
        TestSubscriber<Void> test = TestSubscriber.create();
        listViewModel.fabClicks().subscribe(test);

        test.assertNoValues();
    }

    @Test
    public void fabClick_shouldComplete() {
        TestSubscriber<Void> test = TestSubscriber.create();
        listViewModel.fabClicks().subscribe(test);

        listViewModel.onFabClick();

        test.assertValue(null);
    }
}
