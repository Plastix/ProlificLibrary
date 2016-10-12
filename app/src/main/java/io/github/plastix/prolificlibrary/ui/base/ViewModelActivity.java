package io.github.plastix.prolificlibrary.ui.base;

import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Activity that
 * @param <T> ViewModel type
 * @param <B> DataBinding type
 */
public abstract class ViewModelActivity<T extends AbstractViewModel, B extends ViewDataBinding> extends BaseActivity
        implements LoaderManager.LoaderCallbacks<T> {

    // Internal ID to reference the Loader from the LoaderManager
    private static final int LOADER_ID = 1;

    protected B binding;

    protected T viewModel;

    @Inject
    protected Provider<ViewModelLoader<T>> viewModelFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();

        initLoader();
    }

    protected abstract B getViewBinding();

    private void initLoader() {
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

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

    @Override
    public Loader<T> onCreateLoader(int id, Bundle args) {
        return viewModelFactory.get();
    }

    @Override
    public void onLoadFinished(Loader<T> loader, T data) {
        this.viewModel = data;
    }

    @Override
    public void onLoaderReset(Loader<T> loader) {
        this.viewModel = null;
    }
}
