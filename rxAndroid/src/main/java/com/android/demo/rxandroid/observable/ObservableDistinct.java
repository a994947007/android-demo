package com.android.demo.rxandroid.observable;

import com.android.demo.rxandroid.function.Function;
import com.android.demo.rxandroid.observer.BaseObserver;
import com.android.demo.rxandroid.observer.Observer;

import java.io.IOException;

public class ObservableDistinct<T, K>  extends AbstractObservableWithUpStream<T, T>{

    private final Function<T, K> func;

    public ObservableDistinct(Observable<T> source, Function<T, K> func) {
        super(source);
        this.func = func;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribe(new DistinctObserver<>(observer, func));
    }

    private static class DistinctObserver<T, K> extends BaseObserver<T, T> {

        private K last;

        private final Function<T, K> func;

        public DistinctObserver(Observer<T> actual, Function<T, K> func) {
            super(actual);
            this.func = func;
        }

        @Override
        public void onNext(T t) {
            try {
                K k = func.apply(t);
                if (k.equals(last)) {
                    return;
                }
                last = k;
                actual.onNext(t);
            } catch (IOException e) {
                onError(e);
            }
        }
    }
}
