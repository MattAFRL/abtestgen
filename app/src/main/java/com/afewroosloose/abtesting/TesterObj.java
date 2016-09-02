package com.afewroosloose.abtesting;

import android.app.Activity;
import android.util.Log;
import com.afewroosloose.abtesting.api.annotations.TextTest;
import com.afewroosloose.abtesting.lib.ABTester;

/**
 * Created by matt on 2/09/2016.
 */
public class TesterObj {
  @TextTest(testName = "testytest", values = { "hello", "hola" }) Dummy dummy;

  public TesterObj(Activity activity) {
    dummy = new Dummy();
    ABTester.with(activity).from(this).run("testytest");
  }

  public static class Dummy {
    void setText(String text) {
      Log.d(this.getClass().getSimpleName(), text);
    }
  }
}
