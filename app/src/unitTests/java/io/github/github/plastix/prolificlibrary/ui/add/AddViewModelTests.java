package io.github.github.plastix.prolificlibrary.ui.add;

import android.content.res.Resources;
import android.view.View;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;

import io.github.github.plastix.prolificlibrary.util.RxSchedulersOverrideRule;
import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.data.remote.LibraryService;
import io.github.plastix.prolificlibrary.ui.add.AddViewModel;
import io.github.plastix.prolificlibrary.util.StringUtils;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(Enclosed.class)
public class AddViewModelTests {

    public static class OtherTests {

        @Rule
        public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

        @Mock
        LibraryService libraryService;

        @Mock
        Resources resources;

        AddViewModel addViewModel;


        @Before
        public void setUp() {
            MockitoAnnotations.initMocks(this);

            addViewModel = new AddViewModel(libraryService, resources, null);
            addViewModel.bind();
        }

        @Test
        public void noLoadingShownByDefault() {
            Assert.assertEquals(addViewModel.getProgressVisibility().get(), View.GONE);
        }

        @Test
        public void submitDisabledByDefault() {
            Assert.assertEquals(addViewModel.getSubmitEnabled().get(), false);
        }

        @Test
        public void submit_showLoadingAndDisableButton() {
            addViewModel.getBookTitle().set("Some Title");
            addViewModel.getBookAuthor().set("Some Author");
            addViewModel.getBookPublisher().set("Some Publisher");
            addViewModel.getBookCategories().set("Category");

            when(libraryService.submitBook(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(Observable.<Book>never().toSingle());

            addViewModel.submit();

            Assert.assertEquals(addViewModel.getSubmitEnabled().get(), false);
            Assert.assertEquals(addViewModel.getProgressVisibility().get(), View.VISIBLE);

        }

        @Test
        public void submit_correctStateOnSuccessfulSubmission() {
            addViewModel.getBookTitle().set("Some Title");
            addViewModel.getBookAuthor().set("Some Author");
            addViewModel.getBookPublisher().set("Some Publisher");
            addViewModel.getBookCategories().set("Category");

            Book book = mock(Book.class);
            when(libraryService.submitBook(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(Single.just(book));

            TestSubscriber<Book> testSubscriber = TestSubscriber.create();
            addViewModel.onSubmitSuccess().subscribe(testSubscriber);

            addViewModel.submit();

            Assert.assertEquals(addViewModel.getProgressVisibility().get(), View.GONE);
            testSubscriber.assertValue(book);

        }

        @Test
        public void submit_correctStateOnError() {
            addViewModel.getBookTitle().set("Some Title");
            addViewModel.getBookAuthor().set("Some Author");
            addViewModel.getBookPublisher().set("Some Publisher");
            addViewModel.getBookCategories().set("Category");

            Throwable error = new Throwable();
            when(libraryService.submitBook(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(Single.error(error));

            TestSubscriber<Throwable> testSubscriber = TestSubscriber.create();
            addViewModel.networkErrors().subscribe(testSubscriber);

            addViewModel.submit();

            Assert.assertEquals(addViewModel.getProgressVisibility().get(), View.GONE);
            Assert.assertEquals(addViewModel.getSubmitEnabled().get(), true);
            testSubscriber.assertValue(error);
            testSubscriber.assertNoTerminalEvent();
        }

        @Test
        public void getTitle_addTitleByDefault() {
            when(resources.getString(R.string.add_screen_title)).thenReturn("Add Book");

            Assert.assertEquals("Add Book", addViewModel.getScreenTitle());
        }

        @Test
        public void getTitle_editTitleWhenHasBook() {
            addViewModel = new AddViewModel(libraryService, resources, mock(Book.class));
            when(resources.getString(R.string.add_screen_title_edit)).thenReturn("Edit Book");

            Assert.assertEquals("Edit Book", addViewModel.getScreenTitle());
        }

        @Test
        public void submit_updatesBookWhenHasBook() {
            Book book = new Book();
            book.id = 0;
            book.title = "Title";
            book.author = "Author";
            book.publisher = "Publisher";
            book.categories = "Category";
            addViewModel = new AddViewModel(libraryService, resources, book);
            addViewModel.bind();

            when(libraryService.updateBook(anyString(), anyString(), anyString(), anyString(), anyString(), isNull()))
                    .thenReturn(Single.just(book));

            addViewModel.submit();

            verify(libraryService).updateBook(String.valueOf(book.id), book.author, book.categories,
                    book.title, book.publisher, null);

        }

        @Test
        public void submit_addsBookWhenNoBook() {
            addViewModel = new AddViewModel(libraryService, resources, null);
            addViewModel.bind();

            addViewModel.getBookTitle().set("Title");
            addViewModel.getBookAuthor().set("Author");
            addViewModel.getBookPublisher().set("Publisher");
            addViewModel.getBookCategories().set("Category");

            when(libraryService.submitBook(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(Single.just(mock(Book.class)));

            addViewModel.submit();

            verify(libraryService).submitBook("Author", "Category", "Title", "Publisher");
        }

    }

    @RunWith(org.junit.runners.Parameterized.class)
    public static class ParameterTests {


        @Parameterized.Parameter
        public String title;

        @Parameterized.Parameter(value = 1)
        public String author;

        @Parameterized.Parameter(value = 2)
        public String publisher;

        @Parameterized.Parameter(value = 3)
        public String categories;

        @Mock
        LibraryService libraryService;

        @Mock
        Resources resources;

        AddViewModel addViewModel;

        @Parameterized.Parameters
        public static Collection<String[]> input() {
            return Arrays.asList(new String[][]{
                    {"", "", "", ""},
                    {"a", "", "", ""},
                    {"", "a", "", ""},
                    {"", "", "a", ""},
                    {"", "", "", "a"},
                    {"a", "a", "", ""},
                    {"", "", "a", "a"},
                    {"a", "", "a", ""},
                    {"", "a", "", "a"},
                    {"a", "", "", "a"},
                    {"", "a", "a", ""},
                    {"a", "a", "a", ""},
                    {"", "a", "a", "a"},
                    {"a", "a", "", "a"},
                    {"a", "", "a", "a"},
                    {"a", "a", "a", "a"},
            });

        }

        @Before
        public void setUp() {
            MockitoAnnotations.initMocks(this);

            addViewModel = new AddViewModel(libraryService, resources, null);
            addViewModel.bind();
        }

        @Test
        public void shouldButtonBeEnabled() {
            addViewModel.getBookTitle().set(title);
            addViewModel.getBookAuthor().set(author);
            addViewModel.getBookPublisher().set(publisher);
            addViewModel.getBookCategories().set(categories);

            Assert.assertEquals(addViewModel.getSubmitEnabled().get(), enabled());
        }

        private boolean enabled() {
            return StringUtils.isNotNullOrEmpty(title) &&
                    StringUtils.isNotNullOrEmpty(author) &&
                    StringUtils.isNotNullOrEmpty(publisher) &&
                    StringUtils.isNotNullOrEmpty(categories);
        }

        @Test
        public void hasData() {
            addViewModel.getBookTitle().set(title);
            addViewModel.getBookAuthor().set(author);
            addViewModel.getBookPublisher().set(publisher);
            addViewModel.getBookCategories().set(categories);

            Assert.assertEquals(addViewModel.hasData(), data());

        }

        private boolean data() {
            return StringUtils.isNotNullOrEmpty(title) ||
                    StringUtils.isNotNullOrEmpty(author) ||
                    StringUtils.isNotNullOrEmpty(publisher) ||
                    StringUtils.isNotNullOrEmpty(categories);
        }
    }

}
