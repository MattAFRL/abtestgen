package com.afewroosloose.abtesting.lib;

/**
 * Picks a test using a uniform random distribution
 */
public class SplitPicker extends TestPicker {
  @Override
  public int choose(int numberOfTests) {
    return (int) (Math.random() * Integer.MAX_VALUE) % numberOfTests;
  }
}
