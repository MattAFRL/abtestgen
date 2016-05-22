package com.afewroosloose.multivariate.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by matt on 22/05/2016.
 */
@TesterAnnotation
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceTest {
    String testName();
    String method() default "setText";
    int[] values() default {};
}
