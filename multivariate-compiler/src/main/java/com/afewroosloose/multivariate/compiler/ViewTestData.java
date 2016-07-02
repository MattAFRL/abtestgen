package com.afewroosloose.multivariate.compiler;

import javax.lang.model.element.Element;

/**
 * Created by matt on 22/05/2016.
 */
class ViewTestData {

  private final String packageName;
  private final String className;
  private final String testName;
  private final String methodName;
  private final Element elementAttachedTo;
  private final Object[] values;

  public ViewTestData(String packageName, String className, String testName, String methodName,
      Element elementAttachedTo, Object[] values) {
    this.packageName = packageName;
    this.className = className;
    this.testName = testName;
    this.methodName = methodName;
    this.elementAttachedTo = elementAttachedTo;
    this.values = values;
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
}
