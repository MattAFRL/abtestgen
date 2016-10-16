package com.afewroosloose.abtesting.lib;

/**
 * Created by matt on 8/09/2016.
 */
public class ConstantTestPicker extends TestPicker {

  private final int number;

  ConstantTestPicker(int number) {
    this.number = number;
  }

  @Override
  public int choose(int numberOfTests) {
    return Math.min(number, numberOfTests);
  }
}
