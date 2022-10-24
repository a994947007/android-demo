package com.android.demo.rxandroid.observer;

import com.android.demo.rxandroid.disposable.Disposable;

public interface Observer<T> {
    void onNext(T t);

    void onComplete();

    void onError(Throwable r);

    /**
     * 上游传递过来的 disposable
     */
    void onSubscribe(Disposable d);
}
