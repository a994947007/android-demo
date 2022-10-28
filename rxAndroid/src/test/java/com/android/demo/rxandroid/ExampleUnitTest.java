package com.android.demo.rxandroid;

import org.junit.Test;

import static org.junit.Assert.*;

import com.android.demo.rxandroid.observable.Observable;
import com.android.demo.rxandroid.observer.Consumer;

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

    Observable.interval(100, TimeUnit.MILLISECONDS)
            .subscribe(new Consumer<Long>() {
              @Override
              public void accept(Long aLong) {
                System.out.println(aLong);
              }
            });

      try {
          TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }
}