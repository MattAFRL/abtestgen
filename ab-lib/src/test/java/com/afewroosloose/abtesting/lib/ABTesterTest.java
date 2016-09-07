package com.afewroosloose.abtesting.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by matt on 7/09/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 16)
public class ABTesterTest extends TestCase{
  TesterObj obj;
  Context context;
  SharedPreferences mockPrefs;
  SharedPreferences.Editor mockEditor;

  @Before
  public void setUp() throws Exception {
    context = mock(Context.class);
    obj = new TesterObj(context);
    mockPrefs = mock(SharedPreferences.class);
    mockEditor = mock(SharedPreferences.Editor.class);

    when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
    when(mockEditor.remove(anyString())).thenReturn(mockEditor);
    when(mockPrefs.edit()).thenReturn(mockEditor);
    when(mockEditor.putInt(anyString(), anyInt())).thenReturn(mockEditor);
  }

  @SuppressLint("NewApi")
  @Test
  public void testABTesterNotSaved() {
    when(mockPrefs.contains(anyString())).thenReturn(false);

    ABTester.with(context, obj, new ConstantTestPicker(0)).run("testytest");
    verify(mockEditor).apply();
    verify(mockEditor, never()).remove(anyString());
    assertTrue(obj.dummy.text.equals("hello"));

    ABTester.with(context, obj, new ConstantTestPicker(1)).run("testytest");
    assertTrue(obj.dummy.text.equals("hola"));
  }

  @SuppressLint("NewApi")
  @Test
  public void testABTesterAlreadySaved() {
    when(mockPrefs.getInt(anyString(), anyInt())).thenReturn(0);
    when(mockPrefs.contains(anyString())).thenReturn(true);

    ABTester.with(context, obj).run("testytest");
    verify(mockEditor, never()).apply();
    verify(mockEditor, never()).remove(anyString());
    assertTrue(obj.dummy.text.equals("hello"));

    when(mockPrefs.getInt(anyString(), anyInt())).thenReturn(1);

    ABTester.with(context, obj).run("testytest");
    assertTrue(obj.dummy.text.equals("hola"));
  }

  @SuppressLint("NewApi")
  @Test
  public void testABTesterDontSave() {
    when(mockPrefs.getInt(anyString(), anyInt())).thenReturn(0);
    when(mockPrefs.contains(anyString())).thenReturn(false);

    ABTester.with(context, obj, new ConstantTestPicker(0)).doNotRetain().run("testytest");
    verify(mockEditor).remove(anyString());
    verify(mockEditor).apply();
    assertTrue(obj.dummy.text.equals("hello"));

    when(mockPrefs.getInt(anyString(), anyInt())).thenReturn(1);

    ABTester.with(context, obj, new ConstantTestPicker(1)).doNotRetain().run("testytest");
    assertTrue(obj.dummy.text.equals("hola"));
  }

  @SuppressLint("NewApi")
  @Test
  public void testABTesterDontSave2() {
    when(mockPrefs.getInt(anyString(), anyInt())).thenReturn(1);
    when(mockPrefs.contains(anyString())).thenReturn(true);

    ABTester.with(context, obj, new ConstantTestPicker(0)).doNotRetain().run("testytest");
    verify(mockEditor).remove(anyString());
    verify(mockEditor).apply();
    assertTrue(obj.dummy.text.equals("hello"));

    ABTester.with(context, obj, new ConstantTestPicker(1)).doNotRetain().run("testytest");
    assertTrue(obj.dummy.text.equals("hola"));
  }
}
