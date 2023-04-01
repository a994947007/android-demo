package com.android.demo.rxandroid.subjects;

import com.android.demo.rxandroid.observer.BaseObserver;
import com.android.demo.rxandroid.observer.Observer;

import java.util.concurrent.atomic.AtomicReference;

public class PublishSubject<T> extends Subject<T>{

    static final PublishDisposable[] EMPTY = new PublishDisposable[0];
    static final PublishDisposable[] TERMINATED = new PublishDisposable[0];
    final AtomicReference<PublishDisposable<T>[]> subscribes;

    public static <T> PublishSubject<T> create() {
        return new PublishSubject<>();
    }

    public PublishSubject() {
        subscribes = new AtomicReference<PublishDisposable<T>[]>(EMPTY);
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        PublishDisposable<T> ps = new PublishDisposable<>(observer, this);
        observer.onSubscribe(ps);
        if (add(ps)) {
            // 在add完成之后，在另外一个线程dispose了
            if (ps.isDisposable()) {
                remove(ps);
            }
        }
    }

    public void onNext(T t) {
        PublishDisposable<T>[] publishDisposables = subscribes.get();
        if (publishDisposables == TERMINATED || publishDisposables == EMPTY) {
            return;
        }
        if (t == null) {
            return;
        }
        for (PublishDisposable<T> publishDisposable : publishDisposables) {
            publishDisposable.onNext(t);
        }
    }

    private void remove(PublishDisposable<T> ps) {
        for (;;) {
            PublishDisposable<T>[] a = subscribes.get();
            if (a == TERMINATED || a == EMPTY) {
                return;
            }
            int len = a.length;
            int j = -1;
            for (int i = 0; i < len; i++) {
                if (a[i] == ps) {
                    j = i;
                    break;
                }
            }
            if (j < 0) {
                return;
            }
            PublishDisposable<T>[] b;
            if (len == 1) {
                b = EMPTY;
            } else {
                b = new PublishDisposable[len - 1];
                System.arraycopy(a, 0, b, 0, j);
                System.arraycopy(b, j + 1, b, j + 1, len - j - 1);
            }
            if (subscribes.compareAndSet(a, b)) {
                return;
            }
        }
    }

    private boolean add(PublishDisposable<T> ps) {
        for (;;) {
            PublishDisposable<T>[] a = subscribes.get();
            if (a == TERMINATED) {
                return false;
            }
            int len = a.length;
            PublishDisposable<T>[] newPs = new PublishDisposable[len + 1];
            System.arraycopy(a, 0, newPs, 0, len);
            newPs[len] = ps;
            if (subscribes.compareAndSet(a, newPs)) {
                return true;
            }
        }
    }

    @Override
    public boolean hasObservers() {
        return subscribes.get().length != 0;
    }

    @Override
    public boolean hasThrowable() {
        return false;
    }

    @Override
    public boolean hasComplete() {
        return subscribes.get() == TERMINATED;
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }

    private static class PublishDisposable<T> extends BaseObserver<T, T>{

        private final PublishSubject<T> parent;

        public PublishDisposable(Observer<T> actual, PublishSubject<T> parent) {
            super(actual);
            this.parent = parent;
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }

        @Override
        public void dispose() {
            super.dispose();
            parent.remove(this);
        }
    }
}
