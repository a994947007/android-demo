package com.android.demo.rxandroid.observer;

import com.android.demo.rxandroid.disposable.Disposable;

import java.util.Queue;

public class BlockingObserver<T> implements Observer<T>, Disposable {

    public static final Object TERMINATED = new Object();

    public static final Object COMPLETE = new Object();

    private Disposable d;

    protected final Queue<Object> queue;

    public BlockingObserver(Queue<Object> queue) {
        this.queue = queue;
    }

    @Override
    public void dispose() {
        d.dispose();
        d = null;
        queue.offer(TERMINATED);
    }

    @Override
    public boolean isDisposable() {
        return d == null;
    }

    @Override
    public void onNext(T t) {
        queue.offer(t);
    }

    @Override
    public void onComplete() {
        queue.offer(COMPLETE);
    }

    @Override
    public void onError(Throwable r) {
        queue.offer(r);
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
    }
}
