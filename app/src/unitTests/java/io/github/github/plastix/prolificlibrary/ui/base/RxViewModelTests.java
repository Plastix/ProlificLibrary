package io.github.github.plastix.prolificlibrary.ui.base;

import android.content.Context;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.github.plastix.prolificlibrary.ui.base.RxViewModel;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxViewModelTests {

    @Mock
    Context context;

    SomeRxViewModel someRxViewModel;

    @Before
    public void setUp() {
        someRxViewModel = new SomeRxViewModel();
        someRxViewModel.bind(context);
    }

    @Test
    public void noTasksByDefault() {
        Assert.assertEquals(someRxViewModel.foregroundTasks().hasSubscriptions(), false);
        Assert.assertEquals(someRxViewModel.backgroundTasks().hasSubscriptions(), false);
    }

    @Test
    public void unsubscribeOnUnbind_shouldAddForegroundTask() {
        someRxViewModel.unsubscribeOnUnbind(testSubscription());

        Assert.assertEquals(someRxViewModel.foregroundTasks().hasSubscriptions(), true);
    }

    private Subscription testSubscription() {
        return new Subscription() {
            @Override
            public void unsubscribe() {

            }

            @Override
            public boolean isUnsubscribed() {
                return false;
            }
        };
    }

    @Test
    public void unsubscribeOnDestroy() {
        someRxViewModel.unsubscribeOnDestroy(testSubscription());

        Assert.assertEquals(someRxViewModel.backgroundTasks().hasSubscriptions(), true);
    }

    private class SomeRxViewModel extends RxViewModel {

        CompositeSubscription foregroundTasks() {
            return foregroundTasks;
        }

        CompositeSubscription backgroundTasks() {
            return backgroundTasks;
        }
    }

}
