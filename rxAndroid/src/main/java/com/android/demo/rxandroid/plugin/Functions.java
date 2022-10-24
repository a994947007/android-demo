package com.android.demo.rxandroid.plugin;

import com.android.demo.rxandroid.observer.Action;
import com.android.demo.rxandroid.observer.Consumer;

public class Functions {
    public static final Consumer<Object> EMPTY_CONSUMER = new Consumer<Object>() {
        @Override
        public void accept(Object o) {}
    };

    public static final Consumer<Throwable> ERROR_CONSUMER = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable r) {
            RxJavaPlugins.onError(r);
        }
    };

    public static final Action EMPTY_ACTION = new Action() {
        @Override
        public void run() {}
    };

    public static <T> Consumer<T> emptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }
}
