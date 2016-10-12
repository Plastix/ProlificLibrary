package io.github.github.plastix.prolificlibrary.ui.list;

import android.content.Context;
import android.content.Intent;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListViewModelTest {

    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock
    LibraryService libraryService;

    @Mock
    Context context;

    ListViewModel listViewModel;

    TestSubscriber<List<Book>> testSubscriber;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        listViewModel = new ListViewModel(libraryService);
        listViewModel.bind(context);

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
    public void fabClick_shouldStartActivity() {
        listViewModel.onFabClick();

        verify(context).startActivity(any(Intent.class));
    }
}
