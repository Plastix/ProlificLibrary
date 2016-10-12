package io.github.plastix.prolificlibrary.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import org.parceler.Parcels;

import io.github.plastix.prolificlibrary.ApplicationComponent;
import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.data.model.Book;
import io.github.plastix.prolificlibrary.databinding.ActivityDetailBinding;
import io.github.plastix.prolificlibrary.ui.base.ViewModelActivity;
import io.github.plastix.prolificlibrary.util.ActivityUtils;

public class DetailActivity extends ViewModelActivity<DetailViewModel, ActivityDetailBinding> {

    private static final String EXTRA_BOOK = "EXTRA_BOOK";

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
    }

    @Override
    protected ActivityDetailBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_detail);
    }

    @Override
    protected void injectDependencies(ApplicationComponent component) {
        component.plus(new DetailModule(this, book)).injectTo(this);
    }
}
