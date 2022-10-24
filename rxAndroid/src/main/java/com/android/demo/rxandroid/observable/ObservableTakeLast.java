package com.android.demo.rxandroid.observable;

import java.util.ArrayDeque;

import com.android.demo.rxandroid.observer.BaseObserver;
import com.android.demo.rxandroid.observer.Observer;

public class ObservableTakeLast<T> extends AbstractObservableWithUpStream<T, T>{

  private final int count;

  public ObservableTakeLast(Observable<T> source, int count) {
    super(source);
    this.count = count;
  }

  @Override
  public void subscribeActual(Observer<T> observer) {
    source.subscribeActual(new TakeLastObserver<>(observer, count));
  }

  private static class TakeLastObserver<T> extends BaseObserver<T, T> {

    private final int count;

    private final ArrayDeque<T> mDeque;

    private volatile boolean isCanceled;

    public TakeLastObserver(Observer<T> actual, int count) {
      super(actual);
      this.count = count;
      this.mDeque = new ArrayDeque<>(count);
    }

    @Override
    public void onNext(T t) {
      if (count == mDeque.size()) {
        if (isCanceled) {
          return;
        }
        mDeque.poll();
      }
      mDeque.offer(t);
    }

    @Override
    public void onComplete() {
      Observer<T> a = actual;
      while (!mDeque.isEmpty()) {
        T t = mDeque.poll();
        a.onNext(t);
      }
      a.onComplete();
    }

    @Override
    public void dispose() {
      isCanceled = true;
      super.dispose();
    }
  }
}
