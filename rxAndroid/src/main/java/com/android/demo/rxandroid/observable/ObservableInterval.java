package com.android.demo.rxandroid.observable;

import com.android.demo.rxandroid.disposable.Disposable;
import com.android.demo.rxandroid.observer.BaseObserver;
import com.android.demo.rxandroid.observer.Observer;
import com.android.demo.rxandroid.schedule.Scheduler;

import java.util.concurrent.TimeUnit;

public class ObservableInterval extends Observable<Long>{

    private final long initDelay;
    private final long period;
    private final TimeUnit unit;
    private final Scheduler scheduler;

    public ObservableInterval(long initDelay, long period, TimeUnit unit, Scheduler scheduler) {
        this.initDelay = initDelay;
        this.period = period;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    @Override
    public void subscribeActual(Observer<Long> observer) {
        IntervalObserver intervalObserver = new IntervalObserver(observer);
        observer.onSubscribe(intervalObserver);
        Disposable disposable = scheduler.schedulePeriodicallyDirect(intervalObserver, initDelay, period, unit);
        intervalObserver.setDisposable(disposable);
    }

    private static class IntervalObserver extends BaseObserver<Long, Long> implements Runnable{

        long count = 0;

        public IntervalObserver(Observer<Long> actual) {
            super(actual);
        }

        @Override
        public void onNext(Long aLong) {
            super.onNext(aLong);
        }

        @Override
        public void run() {
            actual.onNext(count ++);
        }
    }
}
