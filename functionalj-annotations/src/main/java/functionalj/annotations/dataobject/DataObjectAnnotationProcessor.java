//  ========================================================================
//  Copyright (c) 2017 Nawapunth Manusitthipol (NawaMan).
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
package functionalj.annotations.dataobject;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
//import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import functionalj.annotations.DataObject;
import functionalj.annotations.Require;
import functionalj.annotations.dataobject.generator.DataObjectBuilder;
import functionalj.annotations.dataobject.generator.Getter;
import functionalj.annotations.dataobject.generator.SourceSpec;
import functionalj.annotations.dataobject.generator.SourceSpec.Configurations;
import functionalj.annotations.dataobject.generator.Type;
import functionalj.annotations.dataobject.generator.model.GenDataObject;
import lombok.val;

/**
 * Annotation processor for DataObject.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DataObjectAnnotationProcessor extends AbstractProcessor {

    private Elements elementUtils;
//    private Types    typeUtils;
    private Filer    filer;
    private Messager messager;
    private boolean  hasError;
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        elementUtils = processingEnv.getElementUtils();
        filer        = processingEnv.getFiler();
        messager     = processingEnv.getMessager();
    }
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(DataObject.class.getCanonicalName());
        return annotations;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    
    private void error(Element e, String msg) {
        hasError = true;
        messager.printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        hasError = false;
        for (Element element : roundEnv.getElementsAnnotatedWith(DataObject.class)) {
            val packageName    = extractPackageName(element);
            val specTargetName = extractTargetTypeName(element);
            try {
                val sourceSpec = extractSourceSpec(element);
                if (sourceSpec == null)
                    continue;
                
                val dataObjSpec = new DataObjectBuilder(sourceSpec).build();
                val className   = (String)dataObjSpec.type().fullName("");
                val content     = new GenDataObject(dataObjSpec).lines().collect(joining("\n"));
                generateCode(element, className, content);
            } catch (Exception e) {
                error(element, "Problem generating the class: "
                                + packageName + "." + specTargetName
                                + ": "  + e.getMessage()
                                + ":"   + e.getClass()
                                + " @ " + e.getStackTrace()[0]);
            }
        }
        return hasError;
    }
    
    private String extractPackageName(Element element) {
        if (element instanceof TypeElement)
            return extractPackageNameType(element);
        if (element instanceof ExecutableElement)
            return extractPackageNameMethod(element);
        throw new IllegalArgumentException("DataObject annotation is only support class or method.");
    }
    private String extractPackageNameType(Element element) {
        val type        = (TypeElement)element;
        val packageName = elementUtils.getPackageOf(type).getQualifiedName().toString();
        return packageName;
    }
    private String extractPackageNameMethod(Element element) {
        val method      = (ExecutableElement)element;
        val type        = (TypeElement)(method.getEnclosingElement());
        val packageName = elementUtils.getPackageOf(type).getQualifiedName().toString();
        return packageName;
    }
    
    private String extractTargetTypeName(Element element) {
        val dataObject     = element.getAnnotation(DataObject.class);
        val specTargetName = dataObject.name();
        return specTargetName;
    }
    
    private SourceSpec extractSourceSpec(Element element) {
        if (element instanceof TypeElement)
            return extractSourceSpecType(element);
        if (element instanceof ExecutableElement)
            return extractSourceSpecMethod(element);
        throw new IllegalArgumentException("DataObject annotation is only support class or method.");
    }
    private SourceSpec extractSourceSpecType(Element element) {
        val type        = (TypeElement)element;
        val simpleName  = type.getSimpleName().toString();
        val isInterface = ElementKind.INTERFACE.equals(element.getKind());
        val isClass     = ElementKind.CLASS    .equals(element.getKind());
        if (!isInterface && !isClass) {
            error(element, "Only a class or interface can be annotated with " + DataObject.class.getSimpleName() + ": " + simpleName);
            return null;
        }
        
        List<Getter> getters = type.getEnclosedElements().stream()
                .filter(elmt  ->elmt.getKind().equals(ElementKind.METHOD))
                .map   (elmt  ->((ExecutableElement)elmt))
                .filter(method->!method.isDefault())
                .filter(method->!isClass || isAbstract(method))
                .filter(method->!(method.getReturnType() instanceof NoType))
                .filter(method->method.getParameters().isEmpty())
                .map   (method->createGetterFromMethod(element, method))
                .collect(toList());
        
        val packageName    = elementUtils.getPackageOf(type).getQualifiedName().toString();
        val sourceName     = type.getQualifiedName().toString().substring(packageName.length() + 1 );
        val dataObject     = element.getAnnotation(DataObject.class);
        val specTargetName = dataObject.name();
        val targetName     = ((specTargetName == null) || specTargetName.isEmpty()) ? simpleName : specTargetName;
        val specField      = dataObject.specField();
        
        val configures = extractConfigurations(element, dataObject);
        if (configures == null)
            return null;
        
        if (!ensureNoArgConstructorWhenRequireFieldExists(element, getters, packageName, specTargetName, configures))
            return null;
        
        try {
            return new SourceSpec(sourceName, packageName, targetName, packageName, isClass, specField, configures, getters);
        } catch (Exception e) {
            error(element, "Problem generating the class: "
                            + packageName + "." + specTargetName
                            + ": "  + e.getMessage()
                            + ":"   + e.getClass()
                            + " @ " + e.getStackTrace()[0]);
            return null;
        }
    }
    
    private SourceSpec extractSourceSpecMethod(Element element) {
//        error(element, elementUtils.getDocComment(element));
        
        val method         = (ExecutableElement)element;
        val packageName    = extractPackageName(element);
        val simpleName     = ((TypeElement)method.getEnclosingElement()).getSimpleName().toString();
        val dataObject     = element.getAnnotation(DataObject.class);
        val specTargetName = dataObject.name().isEmpty() ? method.getSimpleName().toString() : dataObject.name();
        val targetName     = ((specTargetName == null) || specTargetName.isEmpty()) ? simpleName : specTargetName;
        val specField      = dataObject.specField();
        
        // TODO - Make the generated class extends or implements the return type.
        //        The challenge are:
        //          - check if it is class or interface.
        //          - deal with generic.
//        val hasReturnType  = !method.getReturnType().toString().equals("void");
//        error(element, typeUtils.asElement(method.getReturnType()).getKind().toString());
//        val isClass        = hasReturnType ? false : (Boolean)null;//method.getReturnType().toString().equals("void") ? null : typeUtils.asElement(method.getReturnType()).getKind() == ElementKind.CLASS;
//        val superName      = (String)null;
//        val superPackage   = (String)null;
        val isClass      = (Boolean)null;
        val superName    = (String)null;
        val superPackage = packageName;
        
        val configures = extractConfigurations(element, dataObject);
        if (configures == null)
            return null;
        
        val getters = method.getParameters().stream().map(p -> createGetterFromParameter(element, p)).collect(toList());
        
        if (!ensureNoArgConstructorWhenRequireFieldExists(element, getters, packageName, specTargetName, configures))
            return null;
        
        try {
            return new SourceSpec(superName, superPackage, targetName, packageName, isClass, specField, configures, getters);
        } catch (Exception e) {
            error(element, "Problem generating the class: "
                            + packageName + "." + specTargetName
                            + ": "  + e.getMessage()
                            + ":"   + e.getClass()
                            + " @ " + e.getStackTrace()[0]);
            return null;
        }
    }

    private boolean ensureNoArgConstructorWhenRequireFieldExists(Element element, List<Getter> getters,
            final java.lang.String packageName, final java.lang.String specTargetName,
            final functionalj.annotations.dataobject.generator.SourceSpec.Configurations configures) {
        if (!configures.generateNoArgConstructor)
            return true;
        if (getters.stream().noneMatch(g->g.isRequired()))
            return true;
        val field = getters.stream().filter(g->g.isRequired()).findFirst().get().getName();
        error(element, "No arg constructor cannot be generate when at least one field is require: "
                        + packageName + "." + specTargetName + " -> field: " + field);
        return false;
    }
    
    private Getter createGetterFromParameter(Element element, VariableElement p){
        val name = p.getSimpleName().toString();
        val type = getType(element, p.asType());
        val req  = (p.getAnnotation(Require.class) == null) || p.getAnnotation(Require.class).value();
        val getter = new Getter(name, type, req);
        return getter;
    }
    
    private Configurations extractConfigurations(Element element, DataObject dataObject) {
        val configures = new Configurations();
        configures.coupleWithDefinition            = dataObject.coupleWithDefinition();
        configures.generateNoArgConstructor        = dataObject.generateNoArgConstructor();
        configures.generateRequiredOnlyConstructor = dataObject.generateRequiredOnlyConstructor();
        configures.generateAllArgConstructor       = dataObject.generateAllArgConstructor();
        configures.generateLensClass               = dataObject.generateLensClass();
        configures.generateBuilderClass            = dataObject.generateBuilderClass();
        configures.publicFields                    = dataObject.publicFields();

        if (!configures.generateNoArgConstructor
         && !configures.generateAllArgConstructor) {
            error(element, "generateNoArgConstructor and generateAllArgConstructor must be be false at the same time.");
            return null;
        }
        return configures;
    }
    
    private boolean isAbstract(ExecutableElement method) {
        // Seriously ... no other way?
        try (val writer = new StringWriter()) {
            elementUtils.printElements(writer, method);
            return writer.toString().contains(" abstract ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Getter createGetterFromMethod(Element element, ExecutableElement method) {
        val methodName = method.getSimpleName().toString();
        val returnType = getType(element, method.getReturnType());
        val req        = (method.getAnnotation(Require.class) == null) || method.getAnnotation(Require.class).value();
        val getter     = new Getter(methodName, returnType, req);
        return getter;
    }
    
    private Type getType(Element element, TypeMirror typeMirror) {
        val typeStr = typeMirror.toString();
        if (typeMirror instanceof PrimitiveType)
            return Type.primitiveTypes.get(typeStr);
            
        if (typeMirror instanceof DeclaredType) {
            val typeElement = ((TypeElement)((DeclaredType)typeMirror).asElement());
            val typeName = typeElement.getSimpleName().toString();
            if (typeName.equals("String"))
                return Type.STRING;
            
            val generics = ((DeclaredType)typeMirror).getTypeArguments().stream()
                    .map(typeArg -> getType(element, (TypeMirror)typeArg))
                    .collect(toList());
            
            val packageName = getPackageName(element, typeElement);
            return new Type(null, typeName, packageName, generics);
        }
        return null;
    }
    
    private String getPackageName(Element element, TypeElement typeElement) {
        val typePackage = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        if (!typePackage.isEmpty())
            return typePackage;
        
        val packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        return packageName;
    }
    
    private void generateCode(Element element, String className, String content) throws IOException {
        try (Writer writer = filer.createSourceFile(className, element).openWriter()) {
            writer.write(content);
        }
    }
    
}
