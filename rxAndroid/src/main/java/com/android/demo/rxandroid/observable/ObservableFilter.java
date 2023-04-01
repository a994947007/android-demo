package com.android.demo.rxandroid.observable;

import com.android.demo.rxandroid.filter.Predicate;
import com.android.demo.rxandroid.observer.BaseObserver;
import com.android.demo.rxandroid.observer.Observer;

public class ObservableFilter<T> extends AbstractObservableWithUpStream<T, T> {

    private final Predicate<T> filter;

    public ObservableFilter(Observable<T> source, Predicate<T> filter) {
        super(source);
        this.filter = filter;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribe(new FilterObserver<T>(observer, filter));
    }

    private static class FilterObserver<T> extends BaseObserver<T, T> {

        private final Predicate<T> filter;

        public FilterObserver(Observer<T> actual, Predicate<T> filter) {
            super(actual);
            this.filter = filter;
        }

        @Override
        public void onNext(T t) {
            try {
                if (filter.test(t)) {
                    actual.onNext(t);
                }
            } catch (Throwable r) {
                actual.onError(r);
                dispose();
            }
        }
    }
}
