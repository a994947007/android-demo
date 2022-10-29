package com.android.demo.rxandroid;

import org.junit.Test;

import static org.junit.Assert.*;

import com.android.demo.rxandroid.observable.Observable;
import com.android.demo.rxandroid.observer.Consumer;
import com.android.demo.rxandroid.schedule.Schedules;

import java.util.concurrent.TimeUnit;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test
  public void addition_isCorrect() {
    assertEquals(4, 2 + 2);

    Observable.just(1, 2, 3, 4, 5)
            .subscribeOn(Schedules.IO)
            .blockingSubscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) {
                    System.out.println(integer);
                }
            });

  }
}