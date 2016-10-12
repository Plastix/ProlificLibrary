package io.github.github.plastix.prolificlibrary.ui.base;

import android.content.Context;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.plastix.prolificlibrary.ui.base.AbstractViewModel;

public class AbstractViewModelTests {

    @Mock
    Context context;

    SomeViewModel someViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        someViewModel = new SomeViewModel();
    }

    @Test
    public void contextIsNullByDefault() {
        Assert.assertEquals(someViewModel.getContext(), null);
    }

    @Test
    public void bindSetsContext() {
        someViewModel.bind(context);
        Assert.assertEquals(someViewModel.getContext(), context);
    }

    @Test
    public void unbindRemovesContext() {
        someViewModel.bind(context);
        someViewModel.unbind();
        Assert.assertEquals(someViewModel.getContext(), null);
    }

    private class SomeViewModel extends AbstractViewModel {

        // Accessor for tests
        Context getContext() {
            return context;
        }
    }
}
