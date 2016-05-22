package com.afewroosloose.multivariate.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Created by matt on 22/05/2016.
 */
public class ViewTestData {

    private final String packageName;
    private final String className;
    private final String testName;
    private final String methodName;
    private final Element elementAttachedTo;
    private final Object[] values;
    private final DataType dataType;

    public ViewTestData(String packageName, String className, String testName, String methodName, Element elementAttachedTo, Object[] values, DataType dataType) {
        this.packageName = packageName;
        this.className = className;
        this.testName = testName;
        this.methodName = methodName;
        this.elementAttachedTo = elementAttachedTo;
        this.values = values;
        this.dataType = dataType;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getTestName() {
        return testName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getValues() {
        return values;
    }

    public String getTestClassPath() {
        return String.format("%s.%s$$%s", packageName, className, testName);
    }

    public String getFullClassName() {
        return className + "$$" + testName;
    }

    public Element getElementAttachedTo() {
        return elementAttachedTo;
    }

    public DataType getDataType() {
        return dataType;
    }
}
