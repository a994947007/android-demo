package com.android.demo.rxandroid.observer;

public class BlockingFirstObserver<T> extends BlockingBaseObserver<T>{
    public BlockingFirstObserver() {
        super();
    }

    @Override
    public void onNext(T t) {
        if (value == null) {
            value = t;
            dispose();              // 只完成first，完成后需要dispose
            mLatch.countDown();
        }
    }

    @Override
    public void onError(Throwable r) {
        value = null;
        e = r;
        mLatch.countDown();
    }
}
