package com.android.demo.rxandroid.observable;

import com.android.demo.rxandroid.filter.Predicate;
import com.android.demo.rxandroid.observer.BaseObserver;
import com.android.demo.rxandroid.observer.Observer;

public class ObservableTakeUntil<T> extends AbstractObservableWithUpStream<T, T> {

    private final Predicate<T> predicate;

    public ObservableTakeUntil(Observable<T> source, Predicate<T> predicate) {
        super(source);
        this.predicate = predicate;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        TakeUntilObserver<T> takeUntilObserver = new TakeUntilObserver<>(observer, predicate);
        source.subscribe(takeUntilObserver);
    }

    private static class TakeUntilObserver<T> extends BaseObserver<T, T> {

        private final Predicate<T> predicate;
        private boolean done;

        public TakeUntilObserver(Observer<T> actual, Predicate<T> predicate) {
            super(actual);
            this.predicate = predicate;
        }

        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
            if (predicate.test(t)) {
                done = true;
                dispose();
                actual.onComplete();
            } else {
                try {
                    actual.onNext(t);
                } catch (Throwable e) {
                    dispose();
                    onError(e);
                }
            }
        }

        @Override
        public void onError(Throwable r) {
            if (done) {
                return;
            }
            done = true;
            super.onError(r);
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            done = true;
            super.onComplete();
        }
    }
}
