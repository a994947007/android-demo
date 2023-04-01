package com.android.demo.rxandroid.subjects;

import com.android.demo.rxandroid.disposable.EmptyDisposable;
import com.android.demo.rxandroid.observer.BaseObserver;
import com.android.demo.rxandroid.observer.Observer;

import java.util.concurrent.atomic.AtomicReference;

public class BehaviorSubject<T> extends Subject<T> {

    static final BehaviorDisposable[] EMPTY = new BehaviorDisposable[0];
    static final BehaviorDisposable[] TERMINATED = new BehaviorDisposable[0];

    private final AtomicReference<BehaviorDisposable<T>[]> subscribes;
    private final AtomicReference<T> value;

    public BehaviorSubject() {
        subscribes = new AtomicReference<BehaviorDisposable<T>[]>(EMPTY);
        value = new AtomicReference<>();
    }

    public static <T> BehaviorSubject<T> create() {
        return new BehaviorSubject<>();
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        BehaviorDisposable<T> bd = new BehaviorDisposable<>(observer, this);
        observer.onSubscribe(bd);
        if (add(bd)) {
            if (bd.isDisposable()) {
                remove(bd);
            } else {
                bd.emitFirst();
            }
        }
    }

    public T getValue() {
        return value.get();
    }

    public void onNext(T t) {
        BehaviorDisposable<T>[] bds = subscribes.get();
        if (bds == TERMINATED) {
            return;
        }
        if (t == null) {
            return;
        }
        value.lazySet(t);
        for (BehaviorDisposable<T> bd : bds) {
            bd.onNext(t);
        }
    }

    private boolean add(BehaviorDisposable<T> bd) {
        for (;;) {
            BehaviorDisposable<T>[] a = subscribes.get();
            if (a == TERMINATED) {
                return false;
            }
            int len = a.length;
            BehaviorDisposable<T>[] b = new BehaviorDisposable[len +1];
            System.arraycopy(a, 0, b, 0, len);
            b[len] = bd;
            if (subscribes.compareAndSet(a, b)) {
                return true;
            }
        }
    }

    private void remove(BehaviorDisposable<T> bd) {
        for (;;) {
            BehaviorDisposable<T>[] a = subscribes.get();
            if (a == TERMINATED || a == EMPTY) {
                return;
            }
            int len = a.length;
            int j = -1;
            for (int i = 0; i < len; i++) {
                if (a[i] == bd) {
                    j = i;
                    break;
                }
            }
            if (j < 0) {
                return;
            }
            BehaviorDisposable<T>[] b;
            if (len == 1) {
                b = EMPTY;
            } else {
                b = new BehaviorDisposable[len - 1];
                System.arraycopy(a, 0, b, 0, j);
                System.arraycopy(b, j + 1, b, j + 1, len - j - 1);
            }
            if (subscribes.compareAndSet(a, b)) {
                return;
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

    private static class BehaviorDisposable<T> extends BaseObserver<T, T> {

        private boolean next;
        private final BehaviorSubject<T> parent;

        public BehaviorDisposable(Observer<T> actual, BehaviorSubject<T> parent) {
            super(actual);
            this.parent = parent;
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }

        void emitFirst() {
            if (isDisposable()) {
                return;
            }
            T o;
            synchronized (this) {
                if (isDisposable()) {
                    return;
                }
                if (next) {
                    return;
                }
                next = true;
                o = parent.value.get();
            }
            if (o != null) {
                actual.onNext(o);
            }
        }

        @Override
        public void dispose() {
            parent.remove(this);
            actual = EmptyDisposable.emptyObservable();
        }
    }
}
