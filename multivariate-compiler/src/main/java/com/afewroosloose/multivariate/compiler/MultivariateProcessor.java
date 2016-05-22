package com.afewroosloose.multivariate.compiler;

import com.afewroosloose.multivariate.api.annotations.ResourceTest;
import com.afewroosloose.multivariate.api.annotations.TextTest;
import com.google.auto.service.AutoService;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Sets;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class MultivariateProcessor extends AbstractProcessor {

    private static final String VIEW = "android.view.View";
    private static final String ABSTRACT_TEST = "com.afewroosloose.multivariate.api.AbstractTest";

    private static Elements elementUtils;
    private static Filer filer;
    private static Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        filer = env.getFiler();
        elementUtils = env.getElementUtils();
        messager = env.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = Sets.newLinkedHashSet();
        set.add(TextTest.class.getCanonicalName());
        set.add(ResourceTest.class.getCanonicalName());

        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        generateViewTests(annotations, roundEnv);
        return true;
    }

    private void generateViewTests(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Multimap<String, ViewTestData> viewTestMap = MultimapBuilder.linkedHashKeys().arrayListValues().build();
        getTextTests(roundEnv, viewTestMap);
        getResTests(roundEnv, viewTestMap);
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        String[] keys = viewTestMap.keys().elementSet().toArray(new String[0]);
        for (String key : keys) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, key);
            createTest(key, viewTestMap.get(key));
        }
    }

    private void getTextTests(RoundEnvironment roundEnv, Multimap<String, ViewTestData> viewTestMap) {
        for (Element element : roundEnv.getElementsAnnotatedWith(TextTest.class)) {
            TextTest testAnnotation = element.getAnnotation(TextTest.class);
            if (element.getKind() != ElementKind.FIELD) {
                throw new IllegalArgumentException("Can only run ABView tests on a field!");
            } else {
                TypeMirror typeMirror = element.asType();
                String type = typeMirror.toString();
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                ViewTestData data = createABViewData(testAnnotation.testName(), testAnnotation.method(), element, enclosingElement, testAnnotation.values(), DataType.PRIMITIVE);
                viewTestMap.put(data.getTestClassPath(), data);
            }
        }
    }

    private void getResTests(RoundEnvironment roundEnv, Multimap<String, ViewTestData> viewTestMap) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ResourceTest.class)) {
            ResourceTest testAnnotation = element.getAnnotation(ResourceTest.class);
            if (element.getKind() != ElementKind.FIELD) {
                throw new IllegalArgumentException("Can only run ABView tests on a field!");
            } else {
                TypeMirror typeMirror = element.asType();
                String type = typeMirror.toString();
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                Integer[] ints = new Integer[testAnnotation.values().length];
                for (int i = 0; i < ints.length; i++) {
                    ints[i] = testAnnotation.values()[i];
                }
                ViewTestData data = createABViewData(testAnnotation.testName(), testAnnotation.method(), element, enclosingElement, ints, DataType.RESOURCE);
                viewTestMap.put(data.getTestClassPath(), data);
            }
        }
    }

    private void createTest(String name, Collection<ViewTestData> viewTestDatas) {
        ViewTestData[] datas = new ViewTestData[viewTestDatas.size()];
        viewTestDatas.toArray(datas);

        validateTestData(datas);

        MethodSpec numberOfMethods = MethodSpec.methodBuilder("getNumberOfTests") //
                .addModifiers(Modifier.PUBLIC) //
                .addAnnotation(Override.class) //
                .returns(int.class) //
                .addCode("return numberOfTests;\n").build();

        TypeSpec.Builder builder = TypeSpec.classBuilder(datas[0].getFullClassName()) //
                .superclass(ClassName.bestGuess(ABSTRACT_TEST))
                .addField(int.class, "numberOfTests", Modifier.PRIVATE);

        //we make the constructor here
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder();
        for (ViewTestData data : datas) {
            TypeName type = TypeName.get(data.getElementAttachedTo().asType());
            String fieldName = data.getElementAttachedTo().getSimpleName().toString();
            constructorBuilder.addParameter(type, fieldName);
            constructorBuilder.addCode(String.format("this.%s = %s;\n", fieldName, fieldName));
            builder.addField(type, fieldName);
        }
        constructorBuilder.addCode(String.format("numberOfTests = %d;\n", datas[0].getValues().length));
        MethodSpec constructor = constructorBuilder.addModifiers(Modifier.PUBLIC).build();
        builder.addMethod(constructor);

        MethodSpec.Builder spec = MethodSpec.methodBuilder("run") //
                .addModifiers(Modifier.PUBLIC) //
                .addAnnotation(Override.class) //
                .addParameter(int.class, "testToChoose");

        int numberOfPossibilities = datas[0].getValues().length;
        CodeBlock.Builder codeBuilder = CodeBlock.builder();
        for (int i = 0; i < numberOfPossibilities; i++) {
            codeBuilder.beginControlFlow("if (testToChoose == $L)", i);
            for (ViewTestData data : datas) {
                String fieldName = data.getElementAttachedTo().getSimpleName().toString();
                String methodName = data.getMethodName();
                codeBuilder.addStatement("$L.$L($L)", fieldName, methodName,
                        data.getValues()[i].getClass() == String.class ? "\"" + data.getValues()[i] + "\"" : data.getValues()[i]);
            }
            codeBuilder.addStatement("return");
            codeBuilder.endControlFlow();
        }
        spec.addCode(codeBuilder.build());

        TypeSpec typeSpec = builder.addMethod(spec.build()).addMethod(numberOfMethods).addModifiers(Modifier.PUBLIC).build();

        JavaFile javaFile = JavaFile.builder(datas[0].getPackageName(), typeSpec)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateTestData(ViewTestData[] datas) {
        int numberOfParams = datas[0].getValues().length;

        for (ViewTestData data : datas) {
            if (data.getValues().length != numberOfParams) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Each element in a test must have the same number of values!");
                throw new RuntimeException("Each element in a test must have the same number of values!");
            }
            if (data.getElementAttachedTo().getModifiers().contains(Modifier.PRIVATE)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "You can't annotate a private field!");
                throw new RuntimeException("You can't annotate a private field!");
            }
        }
    }

    private ViewTestData createABViewData(String testName, String method, Element element, TypeElement enclosingElement, Object[] values, DataType dataType) {
        return new ViewTestData(getPackageName(enclosingElement), getClassName(enclosingElement, getPackageName(enclosingElement)), testName, method, element, values, dataType);
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private static String getPackageName(TypeElement element) {
        return elementUtils.getPackageOf(element).getQualifiedName().toString();
    }
}
