package com.android.demo.rxandroid.observer;

import com.android.demo.rxandroid.disposable.Disposable;
import com.android.demo.rxandroid.plugin.RxJavaPlugins;

import java.util.concurrent.CountDownLatch;

public abstract class BlockingBaseObserver<T> implements Observer<T>, Disposable {

    protected T value;

    protected Throwable e;

    protected final CountDownLatch mLatch;

    protected Disposable d;

    protected boolean isCanceled;

    public BlockingBaseObserver() {
        mLatch = new CountDownLatch(1);
    }

    @Override
    public void onComplete() {
        mLatch.countDown();
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (isCanceled) {
            d.dispose();
        }
    }

    public final T blockingGet() {
        if (mLatch.getCount() != 0) {
            try {
                mLatch.await();
            } catch (InterruptedException e) {
                dispose();
                RxJavaPlugins.onError(e);
            }
        }
        if (e != null) {
            RxJavaPlugins.onError(e);
        }
        return value;
    }

    @Override
    public void dispose() {
        d.dispose();
        isCanceled = true;
    }

    @Override
    public boolean isDisposable() {
        return isCanceled;
    }
}
