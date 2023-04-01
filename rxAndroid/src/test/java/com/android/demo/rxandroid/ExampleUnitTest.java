package com.android.demo.rxandroid;

import org.junit.Test;

import static org.junit.Assert.*;

import com.android.demo.rxandroid.observable.Observable;
import com.android.demo.rxandroid.observer.Consumer;
import com.android.demo.rxandroid.schedule.Schedules;
import com.android.demo.rxandroid.subjects.BehaviorSubject;
import com.android.demo.rxandroid.subjects.PublishSubject;
import com.android.demo.rxandroid.subjects.Subject;

import java.util.concurrent.TimeUnit;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    BehaviorSubject<String> subject = BehaviorSubject.create();

  @Test
  public void addition_isCorrect() {
    assertEquals(4, 2 + 2);


    subject.onNext("abc");
      subject.subscribe(new Consumer<String>() {
          @Override
          public void accept(String s) {
              System.out.println(s);
          }
      });

  }
}