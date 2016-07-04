package com.afewroosloose.multivariate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import com.afewroosloose.multivariate.api.DefinedTest;
import com.afewroosloose.multivariate.api.annotations.ResourceTest;
import com.afewroosloose.multivariate.api.annotations.TextTest;
import com.afewroosloose.multivariate.lib.MultivariateTester;

@SuppressWarnings("ResourceType")
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @TextTest(testName = "createTests", method = "setText", values = { "hello", "hi" })
  TextView textView;

  @ResourceTest(testName = "createTests", method = "setText",
                values = { R.string.world, R.string.globe })
  TextView textView2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textView = (TextView) findViewById(R.id.text);
    textView2 = (TextView) findViewById(R.id.text2);

    MultivariateTester.with(this).run("createTests").run("complexTests", new DefinedTest() {
      @Override
      public void run() {
        Toast.makeText(MainActivity.this, "These are the contents of a more complex test",
            Toast.LENGTH_LONG).show();
      }
    }, new DefinedTest() {
      @Override
      public void run() {
        Toast.makeText(MainActivity.this, "These are the contents of another more complex test",
            Toast.LENGTH_SHORT).show();
      }
    });
  }
}
