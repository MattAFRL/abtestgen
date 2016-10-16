package com.afewroosloose.abtesting.lib;

import com.afewroosloose.abtesting.api.AbstractTest;

@SuppressWarnings("ResourceType, unused")
public class TesterObj$$testytest extends AbstractTest {
  private int numberOfTests;

  TesterObj.Dummy dummy;

  public TesterObj$$testytest() {
    numberOfTests = 2;
  }

  @Override
  public int getNumberOfTests() {
    return numberOfTests;
  }

  @Override
  public void run(int testToChoose) {
    if (testToChoose == 0) {
      dummy.setText("hello");
      return;
    }
    if (testToChoose == 1) {
      dummy.setText("hola");
      return;
    }
  }
}
