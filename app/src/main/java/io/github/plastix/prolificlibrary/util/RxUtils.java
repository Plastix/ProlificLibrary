package io.github.plastix.prolificlibrary.util;

import android.databinding.ObservableField;

import rx.Emitter;
import rx.Observable;


public class RxUtils {

    private RxUtils() {
    }

    public static <T> Observable<T> toObservable(final ObservableField<T> field) {
        return Observable.fromEmitter(emitter -> {

            final android.databinding.Observable.OnPropertyChangedCallback callback = new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable dataBindingObservable, int propertyId) {
                    if (dataBindingObservable.equals(field)) {
                        emitter.onNext(field.get());
                    }
                }
            };

            field.addOnPropertyChangedCallback(callback);

            emitter.setCancellation(() -> field.removeOnPropertyChangedCallback(callback));

        }, Emitter.BackpressureMode.LATEST);
    }
}
