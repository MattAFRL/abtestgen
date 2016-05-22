package com.afewroosloose.multivariate.lib;

import android.app.Activity;

import com.afewroosloose.multivariate.api.annotations.TextTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * Created by matt on 22/05/2016.
 */
public class MultivariateTester {

    private final Activity activity;

    private MultivariateTester() {
        throw new RuntimeException("Can't call zero-argument constructor");
    }

    private MultivariateTester(Activity activity) {
        this.activity = activity;
    }

    public static MultivariateTester with(Activity activity) {
        return new MultivariateTester(activity);
    }

    public void run() {
        String packageName = activity.getPackageName();
        String className = activity.getClass().getSimpleName();

        Annotation[] annotations = activity.getClass().getAnnotations();
        for (Annotation ann : annotations) {
            if (ann.annotationType() == TextTest.class) {
                runTextTest((TextTest)ann, packageName, className);
            }
        }
    }

    private void runTextTest(TextTest annotation, String packageName, String className) {

    }
}
