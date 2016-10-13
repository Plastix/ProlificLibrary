package io.github.plastix.prolificlibrary.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import org.parceler.Parcels;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.ApplicationComponent;
import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.databinding.ActivityDetailBinding;
import io.github.plastix.prolificlibrary.ui.base.ViewModelActivity;
import io.github.plastix.prolificlibrary.util.ActivityUtils;
import rx.subscriptions.CompositeSubscription;

public class DetailActivity extends ViewModelActivity<DetailViewModel, ActivityDetailBinding> implements CheckoutDialog.Listener {

    private static final String EXTRA_BOOK = "EXTRA_BOOK";

    private final CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    ShareActionProvider shareActionProvider;

    private Book book;

    public static Intent newIntent(Context context, Book book) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_BOOK, Parcels.wrap(book));

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // We need to inject our Book into the Dagger graph
        // Thus we need grab it from the intent before we inject dependencies in super.onCreate()
        book = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_BOOK));
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar);
        ActivityUtils.setBackEnabled(this);
    }

    @Override
    protected void onBind() {
        super.onBind();
        setupUI();
    }

    private void setupUI() {
        binding.setViewModel(viewModel);

        subscriptions.add(viewModel.checkoutClicks()
                .subscribe(this::openCheckoutDialog));

        subscriptions.add(viewModel.checkoutErrors()
                .subscribe(this::showCheckoutErrorSnackbar));

        subscriptions.add(viewModel.successfulCheckOuts()
                .subscribe(this::onSuccessfulCheckout));
    }

    private void openCheckoutDialog(Void ignored) {
        CheckoutDialog.show(this);
    }

    private void showCheckoutErrorSnackbar(Throwable throwable) {
        Snackbar.make(binding.coordinator, R.string.detail_error_checkout, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void onSuccessfulCheckout(Book book) {
        Snackbar.make(binding.coordinator, R.string.detail_checkout_success, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void checkoutDialogClicked(String name) {
        viewModel.checkout(name);
    }

    @Override
    protected ActivityDetailBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_detail);
    }

    @Override
    protected void injectDependencies(ApplicationComponent component) {
        component.plus(new DetailModule(this, book)).injectTo(this);
    }

    @Override
    protected void onUnbind() {
        super.onUnbind();
        subscriptions.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);

        MenuItemCompat.setActionProvider(item, shareActionProvider);

        return true;
    }
}
