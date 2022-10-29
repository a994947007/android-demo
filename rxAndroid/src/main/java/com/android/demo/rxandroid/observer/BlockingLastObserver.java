package com.android.demo.rxandroid.observer;

public class BlockingLastObserver<T> extends BlockingBaseObserver<T>{
    public BlockingLastObserver() {
        super();
    }

    @Override
    public void onNext(T t) {
        value = t;          // 只赋值，不dispose，直到subscribe完成，获取到的一定是最后一个
    }

    @Override
    public void onError(Throwable r) {
        value = null;
        e = r;
        mLatch.countDown();
    }
}
