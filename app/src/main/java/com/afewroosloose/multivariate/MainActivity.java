package com.afewroosloose.multivariate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.afewroosloose.multivariate.api.annotations.ResourceTest;
import com.afewroosloose.multivariate.api.annotations.TextTest;
import com.afewroosloose.multivariate.lib.MultivariateTester;

public class MainActivity extends AppCompatActivity {

    @TextTest(testName = "createTests", method = "setText", values = {"hello", "hi"})
    TextView textView;

    @ResourceTest(testName = "createTests2", method = "setText", values = {R.string.app_name, R.string.hello})
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        textView2 = (TextView) findViewById(R.id.text2);

        MultivariateTester.with(this).run("createTests", "createTests2");
    }
}
