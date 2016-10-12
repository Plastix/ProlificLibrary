package io.github.plastix.prolificlibrary.ui.base;

import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

public abstract class ViewModelActivity<T extends AbstractViewModel, B extends ViewDataBinding> extends BaseActivity {

    protected B binding;

    @Inject
    protected T viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
    }


    protected abstract B getViewBinding();

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.bind(this);
    }

    // On Nougat and above noStop is no longer lazy!!
    // This makes sure to unbind our viewModel properly
    // See https://www.bignerdranch.com/blog/android-activity-lifecycle-onStop/
    @Override
    protected void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            onHidden();
        }
    }

    private void onHidden() {
        viewModel.unbind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            onHidden();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}
