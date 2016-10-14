package io.github.plastix.prolificlibrary.ui.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxViewModel extends AbstractViewModel {

    protected final CompositeSubscription foregroundTasks = new CompositeSubscription();
    protected final CompositeSubscription backgroundTasks = new CompositeSubscription();

    /**
     * Add a subscription to be automatically un-subscribed when the current viewModel is unbound. This
     * is good for cancelling running observables when the viewModel is no longer bound to a visible view.
     *
     * @param subscription
     */
    public void unsubscribeOnUnbind(Subscription subscription) {
        foregroundTasks.add(subscription);
    }

    /**
     * Add a subscription to be automatically un-subscribed when the current viewModel is destroyed. This is
     * good for cancelling running observables that you want to keep updating even though the viewModel
     * isn't attached to a current view.
     *
     * @param subscription
     */
    public void unsubscribeOnDestroy(Subscription subscription) {
        backgroundTasks.add(subscription);
    }

    @Override
    public void onDestroy() {
        backgroundTasks.clear();
    }

    @Override
    public void unbind() {
        super.unbind();
        foregroundTasks.clear();
    }
}
