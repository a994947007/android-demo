package com.android.demo.rxandroid.observable;

import com.android.demo.rxandroid.disposable.Disposable;
import com.android.demo.rxandroid.observer.BaseObserver;
import com.android.demo.rxandroid.observer.Consumer;
import com.android.demo.rxandroid.observer.Observer;

public class ObservableDoOnSubscribe<T> extends AbstractObservableWithUpStream<T, T>{

    private final Consumer<? super Disposable> onSubscribe;

    public ObservableDoOnSubscribe(Observable<T> source, Consumer<? super Disposable> onSubscribe) {
        super(source);
        this.onSubscribe = onSubscribe;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribe(new DoOnSubscribeObserver<>(observer, onSubscribe));
    }

    private static class DoOnSubscribeObserver<T> extends BaseObserver<T, T> {

        private final Consumer<? super Disposable> onSubscribe;

        public DoOnSubscribeObserver(Observer<T> actual, Consumer<? super Disposable> onSubscribe) {
            super(actual);
            this.onSubscribe = onSubscribe;
        }

        @Override
        public void onSubscribe(Disposable d) {
            super.onSubscribe(d);
            onSubscribe.accept(d);
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }
    }
}
