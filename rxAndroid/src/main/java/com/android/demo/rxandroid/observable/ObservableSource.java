package com.android.demo.rxandroid.observable;

import com.android.demo.rxandroid.observer.Observer;

public interface ObservableSource<T> {
    void subscribe(Observer<T> observer);
}
