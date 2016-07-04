# A Multivariate Testing Library for Android...
...that makes writing simple tests simpler by using annotations. 

##How it works 
Currently you can use two different annotations: `@TextTest` and `@ResourceTest`. `@TextTest` is for plugging Strings into a generated test; `@ResourceTest` is for plugging resource IDs. Example usage may be as follows:

    @TextTest(testName="firstTest", method="setText", values={"hello", "world"}
    TextView helloTextView;
    
which will automatically generate a class which you can run by calling `MultivariateTester.with(context).run("firstTest");`

You can also cover multiple fields in the same test, as follows: 

    @TextTest(testName="firstTest", method="setText", values={"hello", "world"}
    TextView helloTextView;
    
    @ResourceTest(testName="firstTest", method="setText", values={R.string.yo, R.string.sup}
    TextView otherTextView;
    
    @TextTest(testName="firstTest", method="setText", values={"elvis", "lives"}
    TextView thirdTextView;
    
And that will generate a class which will create two tests that will cover those three TextViews. 


There's also a `DefinedTest` class which allows you to define more complex tests that may not be possible to generate with the annotation processor.

By default, tests will be weighted to have an equal chance to be selected. However, by extending the `TestPicker` class you can weight the tests however you like. 


