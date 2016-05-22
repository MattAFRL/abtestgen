package com.afewroosloose.multivariate.lib;

import android.app.Activity;

import com.afewroosloose.multivariate.api.AbstractTest;
import com.afewroosloose.multivariate.api.annotations.TextTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public void run(String testName) {
        String packageName = activity.getPackageName();
        String className = activity.getClass().getSimpleName();

        try {
            Class<? extends AbstractTest> testClass = (Class<? extends AbstractTest>) Class.forName(String.format("%s.%s$$%s", packageName, className, testName));
            Field[] fields = testClass.getDeclaredFields();
            List<Object> objects = new ArrayList<>();
            List<Class> classes = new LinkedList<>();
            for (Field field : fields) {
                Field f = activity.getClass().getField(field.getName());
                if (f.isSynthetic()) {
                    continue;
                }
                objects.add(f.get(activity));
                classes.add(f.getType());
            }

            Constructor constructor = testClass.getConstructor(classes.toArray(new Class[0]));
            AbstractTest test = (AbstractTest) constructor.newInstance(objects.toArray());
            test.run(1);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void runTextTest(TextTest annotation, String packageName, String className) {

    }
}
