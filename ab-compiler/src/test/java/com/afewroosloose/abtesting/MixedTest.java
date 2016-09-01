package com.afewroosloose.abtesting;

import com.afewroosloose.abtesting.compiler.MultivariateProcessor;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by matt on 31/08/2016.
 */
public class MixedTest {

  String sourceClass = //
      "package test;" //
          + "\n" //
          + "\nimport ResourceTest;" //
          + "\nimport TextTest;" //
          + "\n" //
          + "\npublic class Test {" //
          + "\n  @TextTest(testName = \"Test1\", values = {\"A\", \"Z\"})" //
          + "\n  Dummy hello;" //
          + "\n" //
          + "\n  @ResourceTest(method = \"setInt\", testName = \"Test1\", values = {0, 1})" //
          + "\n  Dummy bye;" //
          + "\n" //
          + "\n  public static class Dummy {" //
          + "\n    void setText(String value) {" //
          + "\n" //
          + "\n    }" //
          + "\n" //
          + "\n    void setInt(int value) {" //
          + "\n" //
          + "\n    }" //
          + "\n  }" //
          + "\n}"; //

  String generatedClass = //
      "package test;" //
      + "\n" //
      + "\nimport AbstractTest;" //
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
      + "\n      hello.setText(\"A\");"//
      + "\n      bye.setInt(0);" //
      + "\n      return;" //
      + "\n    }" //
      + "\n    if (testToChoose == 1) {" //
      + "\n      hello.setText(\"Z\");" //
      + "\n      bye.setInt(1);" //
      + "\n      return;" //
      + "\n    }" //
      + "\n  }" //
      + "\n}"; //

  @Test
  public void testResourceTest() throws Exception {

    JavaFileObject sourceObj =
        JavaFileObjects.forSourceString("test.Test", sourceClass);

    JavaFileObject genObj =
        JavaFileObjects.forSourceString("test.Test$$Test1", generatedClass);

    assertAbout(javaSource()).that(sourceObj)
        .processedWith(new MultivariateProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(genObj);
  }
}

