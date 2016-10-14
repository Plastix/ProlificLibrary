package io.github.github.plastix.prolificlibrary.ui.detail;

import android.content.res.Resources;
import android.view.View;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;

import io.github.github.plastix.prolificlibrary.util.RxSchedulersOverrideRule;
import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.data.remote.LibraryService;
import io.github.plastix.prolificlibrary.ui.detail.DetailViewModel;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetailViewModelTest {

    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock
    Resources resources;

    @Mock
    LibraryService libraryService;

    Book book;

    DetailViewModel viewModel;

    Calendar calendar;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        calendar = Calendar.getInstance();

        book = new Book();
        book.title = "Some Title";
        book.author = "Some Author";
        book.publisher = "Some Publisher";
        book.categories = "Category 1, Category 2";
        book.checkedOutAuthor = "Some Person";
        calendar.set(2000, 0, 1, 0, 0);
        book.checkedOutDate = calendar.getTime();

        viewModel = new DetailViewModel(book, libraryService, resources);
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
        when(resources.getString(R.string.detail_publisher_text)).thenReturn("Publisher: %1$s");
        Assert.assertEquals("Publisher: " + book.publisher, viewModel.getPublisher());
    }

    @Test
    public void getCategories_returnsCorrectPublisherWhenNull() {
        book.publisher = null;
        when(resources.getString(R.string.detail_publisher_text)).thenReturn("Publisher: %1$s");
        when(resources.getString(R.string.detail_no_publisher)).thenReturn("None");

        Assert.assertEquals("Publisher: None", viewModel.getPublisher());
    }

    @Test
    public void getCategories_returnsCorrectCategoryStringWhenNotNull() {
        when(resources.getString(R.string.detail_categories_text)).thenReturn("Tags: %1$s");

        Assert.assertEquals("Tags: " + book.categories, viewModel.getCategories());
    }

    @Test
    public void getCategories_returnsCorrectCategoryStringWhenNull() {
        book.categories = null;
        when(resources.getString(R.string.detail_categories_text)).thenReturn("Tags: %1$s");
        when(resources.getString(R.string.detail_no_category)).thenReturn("None");

        Assert.assertEquals("Tags: None", viewModel.getCategories());
    }

    @Test
    public void getCheckoutString_returnsCorrectCheckoutString() {
        when(resources.getString(R.string.detail_checkout_text)).thenReturn("Last Checked Out: %1$s @ %2$s");

        Assert.assertEquals("Last Checked Out: Some Person @ January 1, 2000 12:0AM", viewModel.getCheckedOut());
    }

    @Test
    public void getCheckoutString_authorNull() {
        book.checkedOutAuthor = null;
        when(resources.getString(R.string.detail_checkout_text)).thenReturn("Last Checked Out: %1$s @ %2$s");
        when(resources.getString(R.string.detail_no_name)).thenReturn("N/A");

        Assert.assertEquals("Last Checked Out: N/A @ January 1, 2000 12:0AM", viewModel.getCheckedOut());
    }

    @Test
    public void getCheckoutString_dateNull() {
        book.checkedOutDate = null;
        when(resources.getString(R.string.detail_checkout_text)).thenReturn("Last Checked Out: %1$s @ %2$s");
        when(resources.getString(R.string.detail_no_date)).thenReturn("N/A");

        Assert.assertEquals("Last Checked Out: Some Person @ N/A", viewModel.getCheckedOut());
    }

    @Test
    public void getCheckoutString_authorAndDateNull() {
        book.checkedOutDate = null;
        book.checkedOutAuthor = null;
        when(resources.getString(R.string.detail_checkout_text)).thenReturn("Last Checked Out: %1$s @ %2$s");
        when(resources.getString(R.string.detail_no_date)).thenReturn("N/A");
        when(resources.getString(R.string.detail_no_name)).thenReturn("N/A");

        Assert.assertEquals("Last Checked Out: N/A @ N/A", viewModel.getCheckedOut());
    }


    @Test
    public void getShareText_returnsBookTitle() {
        Assert.assertEquals(book.title, viewModel.getShareText());
    }

    @Test
    public void getLoadingVisibility_shouldBeGoneByDefault() {
        Assert.assertEquals(viewModel.getLoadingVisibility().get(), View.GONE);

    }

    @Test
    public void getCheckoutEnabled_shouldBeEnabledByDefault() {
        Assert.assertEquals(viewModel.getCheckoutEnabled().get(), true);

    }

    @Test
    public void getCheckoutClicks_onClickEvent() {
        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();

        viewModel.checkoutClicks().subscribe(testSubscriber);

        viewModel.click();

        testSubscriber.assertValue(null);
        testSubscriber.assertValueCount(1);

    }

    @Test
    public void checkout_emitsSuccessfulCheckout() {
        TestSubscriber<Book> testSubscriber = new TestSubscriber<>();
        String name = "Some Name";
        when(libraryService.updateBook(anyString(), isNull(), isNull(), isNull(),
                isNull(), anyString())).thenReturn(Single.just(book));
        viewModel.successfulCheckOuts().subscribe(testSubscriber);

        viewModel.checkout(name);

        Assert.assertEquals(viewModel.getCheckoutEnabled().get(), true);
        Assert.assertEquals(viewModel.getLoadingVisibility().get(), View.GONE);

        testSubscriber.assertValue(book);
        testSubscriber.assertValueCount(1);

    }

    @Test
    public void checkout_emitsNothing() {
        String name = "Some Name";
        when(libraryService.updateBook(anyString(), isNull(), isNull(), isNull(),
                isNull(), anyString())).thenReturn(
                Observable.<Book>never().toSingle()
        );
        viewModel.checkout(name);

        Assert.assertEquals(viewModel.getCheckoutEnabled().get(), false);
        Assert.assertEquals(viewModel.getLoadingVisibility().get(), View.VISIBLE);

    }

    @Test
    public void checkout_emitsError() {
        TestSubscriber<Throwable> testSubscriber = new TestSubscriber<>();
        String name = "Some Name";
        Throwable error = new Throwable();
        when(libraryService.updateBook(anyString(), isNull(), isNull(), isNull(),
                isNull(), anyString())).thenReturn(
                Single.error(error)
        );

        viewModel.checkoutErrors().subscribe(testSubscriber);

        viewModel.checkout(name);

        Assert.assertEquals(viewModel.getCheckoutEnabled().get(), true);
        Assert.assertEquals(viewModel.getLoadingVisibility().get(), View.GONE);

        testSubscriber.assertValue(error);
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void delete_deletesBook() {
        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();

        when(libraryService.deleteBook(anyInt()))
                .thenReturn(Single.just(null));

        viewModel.deletions().subscribe(testSubscriber);

        viewModel.deleteBook();

        verify(libraryService).deleteBook(book.id);

        testSubscriber.assertValueCount(1);
    }

    @Test
    public void delete_emitsError() {
        TestSubscriber<Throwable> testSubscriber = new TestSubscriber<>();

        Throwable error = new Throwable();

        when(libraryService.deleteBook(anyInt()))
                .thenReturn(Single.error(error));

        viewModel.deleteErrors().subscribe(testSubscriber);

        viewModel.deleteBook();

        verify(libraryService).deleteBook(book.id);

        testSubscriber.assertValue(error);
    }

}
