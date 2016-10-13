package io.github.plastix.prolificlibrary.ui.add;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import io.github.plastix.prolificlibrary.ApplicationComponent;
import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.databinding.ActivityAddBinding;
import io.github.plastix.prolificlibrary.ui.base.ViewModelActivity;
import io.github.plastix.prolificlibrary.util.ActivityUtils;
import rx.subscriptions.CompositeSubscription;

public class AddActivity extends ViewModelActivity<AddViewModel, ActivityAddBinding> {

    private CompositeSubscription subscriptions = new CompositeSubscription();

    public static Intent newIntent(Context context) {
        return new Intent(context, AddActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    public void bookSubmitted() {
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
        component.plus(new AddModule(this)).injectTo(this);
    }
}
