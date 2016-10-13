package io.github.github.plastix.prolificlibrary.util;

import android.databinding.ObservableField;

import org.junit.Before;
import org.junit.Test;

import io.github.plastix.prolificlibrary.util.RxUtils;
import rx.observers.TestSubscriber;

public class RxUtilsTest {

    ObservableField<Boolean> observableField;

    TestSubscriber<Boolean> testSubscriber;

    @Before
    public void setUp() {
        observableField = new ObservableField<>();
        testSubscriber = new TestSubscriber<>();
        RxUtils.toObservable(observableField).subscribe(testSubscriber);
    }

    @Test
    public void toObservable_noEmissionsWhenFieldIsUnchanged() {
        testSubscriber.assertNoTerminalEvent();
        testSubscriber.assertNoValues();
    }

    @Test
    public void toObservable_emissionOfSingleValue() {
        observableField.set(true);

        testSubscriber.assertValue(true);
        testSubscriber.assertNoTerminalEvent();
    }

    @Test
    public void toObservable_emissionOfTwoValues() {
        observableField.set(true);
        observableField.set(false);

        testSubscriber.assertValues(true, false);
        testSubscriber.assertNoTerminalEvent();
    }

    @Test
    public void toObservable_emissionOfThreeValues() {
        observableField.set(true);
        observableField.set(false);
        observableField.set(true);

        testSubscriber.assertValues(true, false, true);
        testSubscriber.assertNoTerminalEvent();
    }
}
