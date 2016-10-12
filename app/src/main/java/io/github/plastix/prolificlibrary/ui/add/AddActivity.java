package io.github.plastix.prolificlibrary.ui.add;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import io.github.plastix.prolificlibrary.ApplicationComponent;
import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.databinding.ActivityAddBinding;
import io.github.plastix.prolificlibrary.ui.base.ViewModelActivity;
import io.github.plastix.prolificlibrary.util.ActivityUtils;

public class AddActivity extends ViewModelActivity<AddViewModel, ActivityAddBinding> {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AddActivity.class);

        return intent;
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
