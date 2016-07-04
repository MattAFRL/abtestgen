# A Multivariate Testing Library for Android...
...that makes writing simple tests simpler by using annotations. 

##How it works 
Currently you can use two different annotations: `@TextTest` and `@ResTest`. `@TextTest` is for plugging Strings into a generated test; `@ResTest` is for plugging resource IDs. Example usage may be as follows:

    @TextTest(testName="firstTest", method="setText", values={"hello", "world"}
    TextView helloTextView;
    
which will automatically generate a class which you can run by calling `MultivariateTester.with(context).run("firstTest");`

You can also cover multiple fields in the same test, as follows: 

    @TextTest(testName="firstTest", method="setText", values={"hello", "world"}
    TextView helloTextView;
    
    @ResTest(testName="firstTest", method="setText", values={R.string.yo, R.string.sup}
    TextView otherTextView;
    
    @TextTest(testName="firstTest", method="setText", values={"elvis", "lives"}
    TextView thirdTextView;
    
And that will generate a class which will create two tests that will cover those three TextViews. 


There's also a `DefinedTest` class which allows you to define more complex tests that may not be possible to generate with the annotation processor.

##To use
To use, you'll want to add the following to your build.gradle:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
	
and add the following dependencies three:

    apt 'com.github.imperial-crystalline-recursion:multivariate-compiler:v0.1'`
    compile 'com.github.imperial-crystalline-recursion:multivariate-lib:v0.1'`
    compile 'com.github.imperial-crystalline-recursion:multivariate-annotations:v0.1'`

You will also require the `gradle-android-apt` plugin. 


