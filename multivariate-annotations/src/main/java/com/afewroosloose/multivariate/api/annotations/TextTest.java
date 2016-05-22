package com.afewroosloose.multivariate.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@TesterAnnotation
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TextTest {
    String testName();
    String method() default "setText";
    String[] values() default {};
}
