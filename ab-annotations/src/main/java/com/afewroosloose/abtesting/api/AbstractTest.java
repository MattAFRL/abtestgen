package com.afewroosloose.abtesting.api;

/**
 * This is for internal use. We use this to generate tests using the annotation processor.
 */
public abstract class AbstractTest {
  public abstract void run(int run);

  public abstract int getNumberOfTests();
}
