# An A/B Testing Library for Android...
...that makes writing simple tests simpler by using annotations.

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-abtestgen-green.svg?style=true)](https://android-arsenal.com/details/1/4301)  [![](https://jitpack.io/v/imperial-crystalline-recursion/abtestgen.svg)](https://jitpack.io/#imperial-crystalline-recursion/abtestgen)


##How it works 
Currently you can use two different annotations: `@TextTest` and `@ResourceTest`. `@TextTest` is for plugging Strings into a generated test; `@ResourceTest` is for plugging resource IDs. Example usage may be as follows:

```java
@TextTest(testName="firstTest", method="setText", values={"hello", "howdy"})
TextView helloTextView;
```
    
which will automatically generate a class which you can run by calling `ABTester.with(activity).run("firstTest");`. Or, if you're running the test from outside of an Activity, `ABTester.with()` can also take a second Object parameter where you've put in the annotations. 

You can also cover multiple fields in the same test, as follows: 
```java
@TextTest(testName="firstTest", method="setText", values={"hello", "howdy"})
TextView helloTextView;

@ResourceTest(testName="firstTest", method="setText", values={R.string.yo, R.string.sup})
TextView otherTextView;

@TextTest(testName="firstTest", method="setText", values={"world", "globe"})
TextView thirdTextView;
```
And that will generate a class which will create two tests that will cover those three TextViews. 


There's also a `CustomTest` class which allows you to define more complex tests that may not be possible to generate with the annotation processor.

By default, tests will be weighted to have an equal chance to be selected. However, by extending the `TestPicker` class you can weight the tests however you like. 

##To obtain...
You'll want to include
```groovy
maven { url "https://jitpack.io" }
```
    
in either your `allprojects.repositories`  or your module's `repositories` section. Whatever you like!

You will also need the APT plugin, so you will want to put this into your root build.gradle's `buildscript.dependencies`:
```groovy
classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
```
and apply this plugin to the modules you're doing your tests in:
```groovy
apply plugin: 'com.neenbedankt.android-apt'
```
Then, you'll want to bang these bad boys into your `build.gradle`s' dependencies:
```groovy
apt 'com.github.imperial-crystalline-recursion.abtestgen:ab-compiler:0.4.1'
compile 'com.github.imperial-crystalline-recursion.abtestgen:ab-annotations:0.4.1'
compile 'com.github.imperial-crystalline-recursion.abtestgen:ab-lib:0.4.1'
```
