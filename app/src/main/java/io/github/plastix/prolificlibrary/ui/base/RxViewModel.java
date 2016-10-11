package io.github.plastix.prolificlibrary.ui.base;

import android.support.annotation.CallSuper;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxViewModel extends AbstractViewModel {

    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

    public void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    @CallSuper
    @Override
    public void unbind() {
        super.unbind();
        compositeSubscription.clear();
    }
}
