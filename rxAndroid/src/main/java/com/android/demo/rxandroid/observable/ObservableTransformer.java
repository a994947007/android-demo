package com.android.demo.rxandroid.observable;

public interface ObservableTransformer<Upstream, Downstream> {

    ObservableSource<Downstream> apply(Observable<Upstream> upstream);
}
