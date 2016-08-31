package com.afewroosloose.multivariate;

import com.afewroosloose.multivariate.compiler.MultivariateProcessor;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Assert;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by matt on 31/08/2016.
 */
public class ResourceTest {

  String sourceClass = //
      "package test;" //
          + "\n" //
          + "\nimport com.afewroosloose.multivariate.api.annotations.ResourceTest;" //
          + "\n" //
          + "\npublic class Test {" //
          + "\n  @ResourceTest(method = \"setInt\", testName = \"Test1\", values = {0, 1})"  //
          + "\n  Dummy hello;" //
          + "\n" //
          + "\n  public static class Dummy {" //
          + "\n    void setInt(int value) {" + "\n" //
          + "\n    }"  //
          + "\n  }" //
          + "\n}"; //

  String generatedClass = //
      "package test;" //
          + "\n"//
          + "\nimport com.afewroosloose.multivariate.api.AbstractTest;" //
          + "\nimport java.lang.Override;"//
          + "\nimport java.lang.SuppressWarnings;" //
          + "\n"//
          + "\n@SuppressWarnings(\"ResourceType, unused\")"//
          + "\npublic class Test$$Test1 extends AbstractTest {" //
          + "\n  private int numberOfTests;"//
          + "\n" //
          + "\n  Test.Dummy hello;" //
          + "\n" //
          + "\n  public Test$$Test1(Test.Dummy hello) {"//
          + "\n    this.hello = hello;" //
          + "\n    numberOfTests = 2;" //
          + "\n  }" //
          + "\n" //
          + "\n  @Override"//
          + "\n  public int getNumberOfTests() {" //
          + "\n    return numberOfTests;" //
          + "\n  }" //
          + "\n" //
          + "\n  @Override" //
          + "\n  public void run(int testToChoose) {"//
          + "\n    if (testToChoose == 0) {" //
          + "\n      hello.setInt(0);" //
          + "\n      return;"//
          + "\n    }" //
          + "\n    if (testToChoose == 1) {" //
          + "\n      hello.setInt(1);"//
          + "\n      return;" //
          + "\n    }" //
          + "\n  }" //
          + "\n}";//

  String sourceClass2 = //
      "package test;" //
          + "\n" //
          + "\nimport com.afewroosloose.multivariate.api.annotations.ResourceTest;" //
          + "\n" //
          + "\npublic class Test {" //
          + "\n  @ResourceTest(method = \"setInt\", testName = \"Test1\", values = {5, 6})" //
          + "\n  Dummy hello;"//
          + "\n" //
          + "\n  @ResourceTest(method = \"setInt\", testName = \"Test1\", values = {3, 4})" //
          + "\n  Dummy bye;" //
          + "\n" //
          + "\n  @ResourceTest(method = \"setInt\", testName = \"Test2\", values = {1, 2})" //
          + "\n  Dummy what;" //
          + "\n" //
          + "\n  public static class Dummy {" //
          + "\n    void setInt(int value) {" //
          + "\n" //
          + "\n    }" //
          + "\n  }" //
          + "\n}";//

  String generatedClass2 = //
      "package test;" //
          + "\n" //
          + "\nimport com.afewroosloose.multivariate.api.AbstractTest;" //
          + "\nimport java.lang.Override;" //
          + "\nimport java.lang.SuppressWarnings;" //
          + "\n" //
          + "\n@SuppressWarnings(\"ResourceType, unused\")" //
          + "\npublic class Test$$Test1 extends AbstractTest {" //
          + "\n  private int numberOfTests;" //
          + "\n" //
          + "\n  Test.Dummy hello;" //
          + "\n" //
          + "\n  Test.Dummy bye;" //
          + "\n" //
          + "\n  public Test$$Test1(Test.Dummy hello, Test.Dummy bye) {" //
          + "\n    this.hello = hello;" //
          + "\n    this.bye = bye;" //
          + "\n    numberOfTests = 2;" //
          + "\n  }" //
          + "\n" //
          + "\n  @Override" //
          + "\n  public int getNumberOfTests() {" //
          + "\n    return numberOfTests;" //
          + "\n  }" //
          + "\n" + "\n  @Override" //
          + "\n  public void run(int testToChoose) {" //
          + "\n    if (testToChoose == 0) {" //
          + "\n      hello.setInt(5);"//
          + "\n      bye.setInt(3);" //
          + "\n      return;" //
          + "\n    }" //
          + "\n    if (testToChoose == 1) {" //
          + "\n      hello.setInt(6);" //
          + "\n      bye.setInt(4);" //
          + "\n      return;" //
          + "\n    }" //
          + "\n  }" //
          + "\n}"; //

  String generatedClass3 = //
      "package test;" //
          + "\n"//
          + "\nimport com.afewroosloose.multivariate.api.AbstractTest;" //
          + "\nimport java.lang.Override;"//
          + "\nimport java.lang.SuppressWarnings;" //
          + "\n"//
          + "\n@SuppressWarnings(\"ResourceType, unused\")"//
          + "\npublic class Test$$Test2 extends AbstractTest {" //
          + "\n  private int numberOfTests;"//
          + "\n" //
          + "\n  Test.Dummy what;" //
          + "\n" //
          + "\n  public Test$$Test2(Test.Dummy what) {"//
          + "\n    this.what = what;" //
          + "\n    numberOfTests = 2;" //
          + "\n  }" //
          + "\n" //
          + "\n  @Override"//
          + "\n  public int getNumberOfTests() {" //
          + "\n    return numberOfTests;" //
          + "\n  }" //
          + "\n" //
          + "\n  @Override" //
          + "\n  public void run(int testToChoose) {"//
          + "\n    if (testToChoose == 0) {" //
          + "\n      what.setInt(1);" //
          + "\n      return;"//
          + "\n    }" //
          + "\n    if (testToChoose == 1) {" //
          + "\n      what.setInt(2);"//
          + "\n      return;" //
          + "\n    }" //
          + "\n  }" //
          + "\n}";//

  String errorSource = //
      "package test;" //
          + "\n" //
          + "\nimport com.afewroosloose.multivariate.api.annotations.ResourceTest;" //
          + "\n" //
          + "\npublic class Test {" //
          + "\n  @ResourceTest(method = \"setInt\", testName = \"Test1\", values = {1, 2})" //
          + "\n  Dummy hello;"//
          + "\n" //
          + "\n  @ResourceTest(method = \"setInt\", testName = \"Test1\", values = {1, 2, 3})" //
          + "\n  Dummy bye;" //
          + "\n" //
          + "\n  public static class Dummy {" //
          + "\n    void setInt(int value) {" //
          + "\n" //
          + "\n    }" //
          + "\n  }" //
          + "\n}";//

  @Test
  public void testResourceTest() {

    JavaFileObject sourceObj =
        JavaFileObjects.forSourceString("test.Test", sourceClass);

    JavaFileObject genObj =
        JavaFileObjects.forSourceString("test.Test$$Test1",
            generatedClass);

    assertAbout(javaSource()).that(sourceObj)
        .processedWith(new MultivariateProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(genObj);

    JavaFileObject sourceObj2 =
        JavaFileObjects.forSourceString("test.Test", sourceClass2);

    JavaFileObject genObj2 =
        JavaFileObjects.forSourceString("test.Test$$Test1",
            generatedClass2);

    JavaFileObject genObj3 =
        JavaFileObjects.forSourceString("test.Test$$Test2",
            generatedClass3);

    assertAbout(javaSource()).that(sourceObj)
        .processedWith(new MultivariateProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(genObj);

    assertAbout(javaSource()).that(sourceObj2)
        .processedWith(new MultivariateProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(genObj2)
        .and()
        .generatesSources(genObj3);

    JavaFileObject errorObj =
        JavaFileObjects.forSourceString("test.Test", errorSource);

    try {
      assertAbout(javaSource()).that(errorObj)
          .processedWith(new MultivariateProcessor())
          .compilesWithoutError();
    } catch (RuntimeException ex) {
      Assert.assertTrue(
          ex.getMessage().contains("Each element in a test must have the same number of values!"));
    }
  }
}

