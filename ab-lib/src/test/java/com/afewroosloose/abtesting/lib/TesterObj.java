package com.afewroosloose.abtesting.lib;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.afewroosloose.abtesting.api.annotations.TextTest;

/**
 * Created by matt on 2/09/2016.
 */
public class TesterObj {
  @TextTest(testName = "testytest", values = { "hello", "hola" }) Dummy dummy;

  public TesterObj(Context context) {
    dummy = new Dummy();
  }

  public static class Dummy {
    String text = null;

    void setText(String text) {
      this.text = text;
    }
  }
}
