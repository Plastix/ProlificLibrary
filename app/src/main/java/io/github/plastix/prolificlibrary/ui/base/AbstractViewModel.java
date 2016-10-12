package io.github.plastix.prolificlibrary.ui.base;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.annotation.CallSuper;

public abstract class AbstractViewModel extends BaseObservable implements ViewModel {

    protected Context context;

    @CallSuper
    @Override
    public void bind(Context context) {
        this.context = context;
    }

    @Override
    public void unbind() {
        this.context = null;
    }

    @Override
    public void onDestroy() {

    }
}
