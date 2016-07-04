package com.afewroosloose.multivariate;

import android.app.Activity;
import android.test.mock.MockContext;
import android.widget.TextView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by matt on 4/07/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TestTester {

  @Test
  public void testTestClass() throws Exception {
    MainActivity activity = Robolectric.buildActivity(MainActivity.class).create().get();
    MainActivity$$createTests test = new MainActivity$$createTests(activity.textView, activity.textView2);

    assertTrue(test.getNumberOfTests() == 2);
    assertTrue(test.textView != null);
    assertTrue(test.textView2 != null);

    test.run(1);
    assertTrue(test.textView.getText().toString().equals("hi"));
    assertTrue(test.textView2.getText().toString().equals("globe"));
    test.run(0);
    assertFalse(test.textView.getText().toString().equals("hi"));
    assertFalse(test.textView2.getText().toString().equals("globe"));
  }
}
