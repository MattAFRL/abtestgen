# An A/B Testing Library for Android...
...that makes writing simple tests simpler by using annotations. 

##How it works 
Currently you can use two different annotations: `@TextTest` and `@ResourceTest`. `@TextTest` is for plugging Strings into a generated test; `@ResourceTest` is for plugging resource IDs. Example usage may be as follows:

    @TextTest(testName="firstTest", method="setText", values={"hello", "world"})
    TextView helloTextView;
    
which will automatically generate a class which you can run by calling `ABTester.with(context).run("firstTest");`

You can also cover multiple fields in the same test, as follows: 

    @TextTest(testName="firstTest", method="setText", values={"hello", "world"})
    TextView helloTextView;
    
    @ResourceTest(testName="firstTest", method="setText", values={R.string.yo, R.string.sup})
    TextView otherTextView;
    
    @TextTest(testName="firstTest", method="setText", values={"elvis", "lives"})
    TextView thirdTextView;
    
And that will generate a class which will create two tests that will cover those three TextViews. 


There's also a `DefinedTest` class which allows you to define more complex tests that may not be possible to generate with the annotation processor.

By default, tests will be weighted to have an equal chance to be selected. However, by extending the `TestPicker` class you can weight the tests however you like. 

##To obtain...
You'll want to include

    maven { url "https://jitpack.io" }
    
in either your `allprojects.repositories`  or your module's `repositories` section. Whatever you like!

You will also need the APT plugin, so you will want to put this into your root build.gradle's `buildscript.dependencies`:

    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'

and apply this plugin to the modules you're doing your tests in:

    apply plugin: 'com.neenbedankt.android-apt'

Then, you'll want to bang these bad boys into your `build.gradle`s' dependencies:

    apt 'com.github.imperial-crystalline-recursion.abtestgen:ab-compiler:0.3.2'
    compile 'com.github.imperial-crystalline-recursion.abtestgen:ab-annotations:0.3.2'
    compile 'com.github.imperial-crystalline-recursion.abtestgen:ab-lib:0.3.2'

##Build status
![Build status](https://circleci.com/gh/imperial-crystalline-recursion/abtestgen.svg?style=shield&circle-token=02adbc662080afafe062fdd8ee467cafa703014b "Build status")

