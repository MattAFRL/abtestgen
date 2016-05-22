package com.afewroosloose.multivariate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.afewroosloose.multivariate.api.annotations.TextTest;

public class MainActivity extends AppCompatActivity {

    @TextTest(testName = "createTests", method = "setText", values = {"hello", "hi"})
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
    }
}
