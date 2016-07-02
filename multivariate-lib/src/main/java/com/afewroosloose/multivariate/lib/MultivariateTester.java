package com.afewroosloose.multivariate.lib;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import com.afewroosloose.multivariate.api.AbstractTest;
import com.afewroosloose.multivariate.api.DefinedTest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by matt on 22/05/2016.
 */
public final class MultivariateTester {

  private final Activity activity;
  private final TestPicker picker;

  private MultivariateTester() {
    throw new RuntimeException("Can't call zero-argument constructor");
  }

  private MultivariateTester(Activity activity) {
    this.activity = activity;
    this.picker = new SplitPicker();
  }

  private MultivariateTester(Activity activity, TestPicker picker) {
    this.activity = activity;
    this.picker = picker;
  }

  public static MultivariateTester with(Activity activity) {
    return new MultivariateTester(activity);
  }

  public static MultivariateTester with(Activity activity, TestPicker picker) {
    return new MultivariateTester(activity, picker);
  }

  public MultivariateTester run(String... testNames) {
    for (String name : testNames) {
      run(name);
    }
    return this;
  }

  public MultivariateTester run(String testName) {
    String packageName = activity.getPackageName();
    String className = activity.getClass().getSimpleName();

    int selection;

    SharedPreferences prefs = activity.getSharedPreferences(className, Context.MODE_PRIVATE);

    try {
      Class<?> testClass =
          Class.forName(String.format("%s.%s$$%s", packageName, className, testName));
      Field[] fields = testClass.getDeclaredFields();
      List<Object> objects = new ArrayList<>();
      List<Class> classes = new ArrayList<>();
      for (Field field : fields) {
        if (Modifier.isPrivate(field.getModifiers()) || field.isSynthetic()) {
          continue; // numberOfTests/synthetic
        }
        Field f = activity.getClass().getDeclaredField(field.getName());
        f.setAccessible(true);
        objects.add(f.get(activity));
        classes.add(f.getType());
      }
      Constructor constructor =
          testClass.getConstructor(classes.toArray(new Class[classes.size()]));
      AbstractTest test = (AbstractTest) constructor.newInstance(objects.toArray());
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

  public MultivariateTester run(String testName, DefinedTest... tests) {
    int selection;

    SharedPreferences prefs =
        activity.getSharedPreferences(activity.getClass().getSimpleName(), Context.MODE_PRIVATE);
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
