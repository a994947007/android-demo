package com.android.demo.rxandroid.subjects;

import com.android.demo.rxandroid.observable.Observable;

public abstract class Subject<T> extends Observable<T> {

    public abstract boolean hasObservers();

    public abstract boolean hasThrowable();

    public abstract boolean hasComplete();

    public abstract Throwable getThrowable();
}
