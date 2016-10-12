package io.github.plastix.prolificlibrary.ui.base;

import android.content.Context;
import android.support.v4.content.Loader;

import javax.inject.Inject;
import javax.inject.Provider;

import io.github.plastix.prolificlibrary.ui.ActivityScope;

public class ViewModelLoader<T extends AbstractViewModel> extends Loader<T> {

    private T viewModel;

    private Provider<T> viewModelFactory;


    @Inject
    public ViewModelLoader(@ActivityScope Context context, Provider<T> viewModelFactory) {
        super(context);
        this.viewModelFactory = viewModelFactory;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        // If we already have a viewModel instance, simply deliver it.
        if (viewModel != null) {
            deliverResult(viewModel);
        } else {
            // Otherwise, force a load
            forceLoad();
        }

    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();

        // Grab an instance of the viewModel from the provider
        viewModel = viewModelFactory.get();

        // Deliver the viewModel
        deliverResult(viewModel);

    }

    @Override
    protected void onReset() {
        super.onReset();

        // Clean up viewModel resources if we have one
        if (viewModel != null) {
            viewModel.onDestroy();
        }
    }
}
