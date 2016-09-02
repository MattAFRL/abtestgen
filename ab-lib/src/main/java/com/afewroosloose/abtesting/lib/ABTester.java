package com.afewroosloose.abtesting.lib;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import com.afewroosloose.abtesting.api.AbstractTest;
import com.afewroosloose.abtesting.api.DefinedTest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by matt on 22/05/2016.
 */
public final class ABTester {

  private final Context context;
  private final TestPicker picker;
  private Object pointOfReference;

  private ABTester() {
    throw new RuntimeException("Can't call zero-argument constructor");
  }

  private ABTester(Context context) {
    this.context = context;
    this.picker = new SplitPicker();
  }

  private ABTester(Context context, TestPicker picker) {
    this.context = context;
    this.picker = picker;
  }

  public static ABTester with(Activity activity) {
    return new ABTester(activity);
  }

  public static ABTester with(Activity activity, TestPicker picker) {
    return new ABTester(activity, picker);
  }

  public static ABTester with(Context context, Object pointOfReference) {
    return new ABTester(context).from(pointOfReference);
  }

  public static ABTester with(Context context, Object pointOfReference, TestPicker testPicker) {
    return new ABTester(context, testPicker).from(pointOfReference);
  }

  /**
   * Specifies an object to use as a point of reference for running an AB test. Typically you'd
   * just
   * pass in <b>this</b> from inside whichever class you're doing the tests in.
   *
   * @param fromObj the object we're running the test from. We use this to grab the class name.
   * @return the ABTester, point-of-reference Object included.
   */
  public ABTester from(Object fromObj) {
    if (pointOfReference != null) {
      throw new IllegalStateException("Can only supply one point of reference!");
    }
    pointOfReference = fromObj;
    return this;
  }

  public ABTester run(String... testNames) {
    for (String name : testNames) {
      run(name);
    }
    return this;
  }

  public ABTester run(String testName) {
    String packageName = context.getPackageName();
    String className;
    Object srcObject = pointOfReference == null ? context : pointOfReference;
    className = srcObject.getClass().getSimpleName();

    int selection;

    SharedPreferences prefs = context.getSharedPreferences(className, Context.MODE_PRIVATE);

    try {
      Class<?> testClass =
          Class.forName(String.format("%s.%s$$%s", packageName, className, testName));
      Field[] fields = testClass.getDeclaredFields();
      Constructor constructor = testClass.getConstructor();
      AbstractTest test = (AbstractTest) constructor.newInstance();
      for (Field f : fields) { // this is the hackiest crap ever but APPARENTLY fields don't get returned in the order in which they're declared.
        if (Modifier.isPrivate(f.getModifiers()) || f.isSynthetic()) {
          continue;
        }
        Field srcField = srcObject.getClass().getDeclaredField(f.getName());
        srcField.setAccessible(true);
        f.setAccessible(true);
        f.set(test, srcField.get(srcObject));
      }
      if (prefs.contains(testName)) {
        selection = prefs.getInt(testName, 0);
      } else {
        selection = picker.choose(test.getNumberOfTests());
        SharedPreferences.Editor ed = prefs.edit().putInt(testName, selection);
        if (Build.VERSION.SDK_INT > 8) {
          ed.apply();
        } else {
          ed.commit();
        }
      }
      test.run(selection);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
    return this;
  }

  public ABTester run(String testName, DefinedTest... tests) {
    int selection;

    SharedPreferences prefs = context.getSharedPreferences(
        pointOfReference == null ? context.getClass().getSimpleName()
            : pointOfReference.getClass().getSimpleName(), Context.MODE_PRIVATE);
    if (prefs.contains(testName)) {
      selection = prefs.getInt(testName, 0);
    } else {
      selection = picker.choose(tests.length);
      SharedPreferences.Editor ed = prefs.edit().putInt(testName, selection);
      if (Build.VERSION.SDK_INT > 8) {
        ed.apply();
      } else {
        ed.commit();
      }
    }
    tests[selection].run();
    return this;
  }
}
