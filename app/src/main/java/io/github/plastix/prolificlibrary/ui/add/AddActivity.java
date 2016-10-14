package io.github.plastix.prolificlibrary.ui.add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import org.parceler.Parcels;

import io.github.plastix.prolificlibrary.ApplicationComponent;
import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.databinding.ActivityAddBinding;
import io.github.plastix.prolificlibrary.ui.base.ViewModelActivity;
import io.github.plastix.prolificlibrary.util.ActivityUtils;
import rx.subscriptions.CompositeSubscription;

public class AddActivity extends ViewModelActivity<AddViewModel, ActivityAddBinding> {

    public final static String EXTRA_BOOK = "EXTRA_BOOK";

    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private Book book;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddActivity.class);
    }

    public static Intent newIntent(Context context, Book book) {
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra(EXTRA_BOOK, Parcels.wrap(book));

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setTitle(viewModel.getScreenTitle());

        subscriptions.add(viewModel
                .networkErrors()
                .subscribe(this::makeErrorSnackbar));

        subscriptions.add(viewModel
                .onSubmitSuccess()
                .subscribe(this::bookSubmitted));
    }

    public void makeErrorSnackbar(Throwable error) {
        Snackbar.make(binding.coordinator, R.string.add_submit_error, Snackbar.LENGTH_SHORT)
                .show();
    }

    public void bookSubmitted(Book book) {
        // If the book was modified, we want to send the updated book back to any previous Activity
        Intent data = new Intent();
        data.putExtra(EXTRA_BOOK, Parcels.wrap(book));
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        // If the user input some data show warning dialog before backing out
        if (viewModel.hasData()) {
            ExitDialog.show(this);
        } else {
            // Allow back to happen normally
            super.onBackPressed();
        }
    }


    @Override
    protected void onUnbind() {
        super.onUnbind();
        subscriptions.clear();
    }

    @Override
    protected ActivityAddBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_add);
    }

    @Override
    protected void injectDependencies(ApplicationComponent component) {
        component.plus(new AddModule(this, book)).injectTo(this);
    }
}
