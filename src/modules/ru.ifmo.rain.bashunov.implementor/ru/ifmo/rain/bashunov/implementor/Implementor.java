package ru.ifmo.rain.bashunov.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import static ru.ifmo.rain.bashunov.implementor.BasicService.*;

/**
 * Implementation of {@code JarImpler}
 *
 * @author Erofey Bashunov
 * @version 0.3
 * @see JarImpler
 */
public class Implementor implements JarImpler {

    private static final String TEMP_PATH = "./tmp";

    /**
     * Modifier with all modifier flags except {@code ABSTRACT},
     * {@code INTERFACE}, {@code TRANSIENT}, {@code VOLATILE}.
     *
     * @see Modifier
     */
    private static final int MODIFIERS_NORMALIZER = ~(Modifier.ABSTRACT + Modifier.INTERFACE + Modifier.TRANSIENT + Modifier.VOLATILE);

    /**
     * Empty constructor.
     */
    public Implementor() {

    }

    /**
     * Run {@code implement} or {@code implementJar} depends on {@code args}.
     *
     * Arguments should be in one of those two forms:
     * {@code [class to implement] [path to save file]} or
     * {@code [-jar] [class to implement] [path to save file].
     * In first case it creates a java-file with implementation of class.
     * In second case it creates a jar-file with java-file and class-file
     * with implementation of class.
     *
     * @param args command-line arguments. Should contains
     *             {@code {[class to implement] [path to save file]}}
     *             or {@code {-jar [class to implement] [path to save jar-file]}}
     */
    public static void main(String[] args) {
        Implementor implementor = new Implementor();
        if (args == null || Arrays.stream(args).anyMatch(Objects::isNull)) {
            writeErrorMessage("Your arguments are incorrect");
            getUsage();
            return;
        }
        try {
            if (args.length == 2) {
                implementor.implement(Class.forName(args[0]), Paths.get(args[1]));
            } else if (args.length == 3 && args[0].equals("-jar")) {
                implementor.implementJar(Class.forName(args[1]), Paths.get(args[2]));
            } else {
                writeErrorMessage("Your arguments are incorrect");
                getUsage();
            }
        } catch (ClassNotFoundException e) {
            writeErrorMessage("First argument is not correct");
            getUsage();
        } catch (InvalidPathException e) {
            writeErrorMessage("Second argument is not correct");
            getUsage();
        } catch (ImplerException e) {
            writeErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create jar-file with implementation of the {@code clazz}
     *
     * It runs {@code implement} and then compiles generated java-file. Then it runs
     * {@code createManifest} to create a manifest file. Finally it creates a
     * zip-file with java-file, class-file and manifest file.
     *
     * @param clazz class to implement
     * @param path  path to save
     * @throws ImplerException if something wrong with {@code path};
     *                         if implement threw {@code ImplerException};
     *                         if there was problems with compiling a java-file;
     *                         if there was problems with creating a zip;
     *                         if any error has occurred
     */
    @Override
    public void implementJar(Class<?> clazz, final Path path) throws ImplerException {
        Path tempDirectoryPath, dotJavaFilePath, dotClassFilePath;
        try {
            System.out.println(Paths.get(TEMP_PATH).toAbsolutePath());
            tempDirectoryPath = Files.createTempDirectory(Paths.get(TEMP_PATH), "temp_hw05_");
        } catch (IOException e) {
            throw new ImplerException("Error while creating temporary directory", e);
        } catch (SecurityException e) {
            throw new ImplerException("Security error", e);
        }
        try {
            Path packagePath = tempDirectoryPath.resolve(clazz.getPackageName().replace(".", SEPARATOR));
            dotJavaFilePath = packagePath.resolve(modifyClassName(clazz, ".java"));
            dotClassFilePath = packagePath.resolve(modifyClassName(clazz, ".class"));
        } catch (InvalidPathException e) {
            throw new ImplerException("Incorrect file path", e);
        }
        implement(clazz, tempDirectoryPath);

        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        if (javaCompiler == null) {
            throw new ImplerException("No compiler is provided");
        }
        String[] args = {dotJavaFilePath.toString(), "-p",
                tempDirectoryPath.toString() + PATH_SEPARATOR + System.getProperty("java.class.path"),
                "-encoding", ENCODING};
        if (javaCompiler.run(null, null, null, args) != 0) {
            throw new ImplerException("Compile error");
        }

        try (JarOutputStream jarOutputWriter = new JarOutputStream(Files.newOutputStream(path), createManifest())) {
            jarOutputWriter.putNextEntry(new ZipEntry(clazz.getName().replace('.', '/') + "Impl.class"));
            Files.copy(dotClassFilePath, jarOutputWriter);
            jarOutputWriter.closeEntry();
        } catch (IllegalArgumentException e) {
            throw new ImplerException("Very long path", e);
        } catch (IOException e) {
            throw new ImplerException("Problems with creating zip file", e);
        } catch (SecurityException e) {
            throw new ImplerException("Security error", e);
        }
        try {
            Files.deleteIfExists(dotJavaFilePath);
            Files.deleteIfExists(dotClassFilePath);
            Files.deleteIfExists(tempDirectoryPath);
        } catch (IOException e) {
            writeErrorMessage("Warning: some temporary files did not deleted");
        }
    }

    /**
     * Implement the {@code clazz} and save it to {@code path}.
     *
     * Implement all unrealized constructors and methods of {@code clazz}.
     * Firstly it creates a correct header of the class.
     * Secondly it runs {@code appendMethod} for all unrealized methods
     * and {@code appendConstructor} for all unrealized constructors.
     * And finally it write it to {@code path}.
     *
     * @param clazz class to implement
     * @param path  path to save
     * @throws ImplerException if something wrong with {@code path};
     * if {@code clazz} is primitive, array, enum, final, if it is utility
     * class or if it is Enum.class; if any error has occurred
     */
    @Override
    public void implement(Class<?> clazz, final Path path) throws ImplerException {
        String className = modifyClassName(clazz, "");
        Path packagePath, filePath;
        try {
            packagePath = path.resolve(clazz.getPackageName().replace(".", SEPARATOR));
            filePath = packagePath.resolve(modifyClassName(clazz, ".java"));
        } catch (InvalidPathException e) {
            throw new ImplerException("Incorrect file path", e);
        }

        if (clazz.isPrimitive()) {
            throw new ImplerException("Primitive cannot be implemented");
        } else if (clazz.isArray()) {
            throw new ImplerException("Array cannot be implemented");
        } else if (clazz.isEnum()) {
            throw new ImplerException("Enum cannot be implemented");
        } else if (Modifier.isFinal(clazz.getModifiers())) {
            throw new ImplerException("Final class cannot be implemented");
        } else if (isUtilityClass(clazz)) {
            throw new ImplerException("Utility class cannot be implemented");
        } else if (clazz.equals(Enum.class)) {
            throw new ImplerException("Enum.class cannot be implemented");
        }

        try {
            Files.createDirectories(packagePath);
        } catch (IOException e) {
            throw new ImplerException("Cannot create package", e);
        }
        try (BufferedWriter outputWriter = Files.newBufferedWriter(filePath)) {
            StringBuilder generatedCode = new StringBuilder();
            
            /* append head of class */
            String packageName = clazz.getPackageName();
            if (!packageName.equals("")) {
                generatedCode.append("package ").append(packageName).append(";").append(DOUBLE_LINE_SEPARATOR);
            }
            generatedCode.append(normalizeModifiers(clazz.getModifiers())).append(" class ").append(className);
            generatedCode.append(clazz.isInterface() ? " implements " : " extends ").append(clazz.getCanonicalName()).append(" ");

            /* append body of class */
            generatedCode.append("{").append(DOUBLE_LINE_SEPARATOR);
            for (Constructor constructor : clazz.getDeclaredConstructors()) {
                if (Modifier.isPrivate(constructor.getModifiers())) continue;
                appendConstructor(generatedCode, constructor);
            }
            iterateParents(generatedCode, clazz, new HashSet<>());
            generatedCode.append("}").append(LINE_SEPARATOR);

            /* write the result */
            outputWriter.write(transformToUnicode(generatedCode).toString());
//            System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            throw new ImplerException("Problems with creating file", e);
        } catch (SecurityException e) {
            throw new ImplerException("Security error", e);
        }
    }

    /**
     * Generate code for unrealized methods and constructors.
     * <p>
     * It recursively finds unrealized methods and constructor in superclasses and interfaces of {@code clazz}.
     * And for all found methods and constructors it runs {@code appendMethod} and {@code appendConstructor}.
     * </p>
     *
     * @param generatedCode {@link StringBuilder} to write generated codes of methods and constructor
     * @param clazz current viewed class
     * @param implementedMethods the set of result of {@code transformToString} for found methods
     * @see Method
     */
    private void iterateParents(StringBuilder generatedCode, Class clazz, HashSet<String> implementedMethods) throws SecurityException {
        if (clazz.equals(Object.class)) return;
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            for (Method method : clazz.getDeclaredMethods()) {
                String methodString = transformToString(method);
                int modifiers = method.getModifiers();
                if (Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers) || method.isDefault() || implementedMethods.contains(methodString))
                    continue;
                implementedMethods.add(methodString);
                appendMethod(generatedCode, method);
            }
            if (clazz.getSuperclass() != null) {
                iterateParents(generatedCode, clazz.getSuperclass(), implementedMethods);
            }
            for (Class superInterface : clazz.getInterfaces()) {
                iterateParents(generatedCode, superInterface, implementedMethods);
            }
        }
    }

    /**
     * Generate default code for {@code method} that only {@code return} its return type's value.
     *
     * @param generatedCode {@link StringBuilder} to write generated code of {@code method}
     * @param method        generating method
     * @see Method
     */
    private void appendMethod(StringBuilder generatedCode, Method method) {
        Class type = method.getReturnType();
        StringBuilder code = new StringBuilder("return");
        code.append((!type.isPrimitive()) ? " null" : ((type.equals(void.class)) ? "" : ((type.equals(boolean.class)) ? " false" : " 0"))).append(";");

        appendExecutableHeader(generatedCode, method, type.getCanonicalName() + " " + method.getName());
        appendExecutableBody(generatedCode, method, code);
    }

    /**
     * Generate default code for {@code constructor} that contains only {@code super} with its parameters.
     *
     * @param generatedCode {@link StringBuilder} to write generated code of {@code constructor}
     * @param constructor   generating constructor
     * @see Constructor
     */
    private void appendConstructor(StringBuilder generatedCode, Constructor constructor) {
        StringBuilder code = new StringBuilder("super(");
        int parametersCount = 0;
        for (Parameter parameter : constructor.getParameters()) {
            if (parametersCount > 0) {
                code.append(", ");
            }
            code.append(parameter.getName());
            parametersCount++;
        }
        code.append(");");

        appendExecutableHeader(generatedCode, constructor, constructor.getDeclaringClass().getSimpleName() + "Impl");
        appendExecutableBody(generatedCode, constructor, code);
    }

    /**
     * Write default header of {@code executable} with {@code name}.
     *
     * @param generatedCode {@link StringBuilder} to write generated code of {@code executable}
     * @param executable    constructor or method
     * @param name          name of {@code executable}
     * @see Executable
     */
    private void appendExecutableHeader(StringBuilder generatedCode, Executable executable, String name) {
        generatedCode.append(INDENT).append(normalizeModifiers(executable.getModifiers())).append(" ");
        generatedCode.append(name).append("(");

        int parametersCount = 0;
        for (Parameter parameter : executable.getParameters()) {
            if (parametersCount > 0) {
                generatedCode.append(", ");
            }
            generatedCode.append(Modifier.toString(parameter.getModifiers())).append(" ");
            generatedCode.append(parameter.getType().getCanonicalName()).append(" ").append(parameter.getName()).append(" ");
            parametersCount++;
        }
        generatedCode.append(")");

        int exceptionsCount = 0;
        for (Class<?> exception : executable.getExceptionTypes()) {
            if (exceptionsCount > 0) {
                generatedCode.append(", ");
            } else {
                generatedCode.append(" throws ");
            }
            generatedCode.append(exception.getCanonicalName());
            exceptionsCount++;
        }
    }

    /**
     * Write default body of {@code executable} with {@code code}.
     *
     * @param generatedCode {@link StringBuilder} to write generated code of {@code executable}
     * @param executable    constructor or method
     * @param code          code for {@code executable}
     * @see Executable
     */
    private void appendExecutableBody(StringBuilder generatedCode, Executable executable, StringBuilder code) {
        if (Modifier.isNative(executable.getModifiers())) {
            generatedCode.append(";");
            return;
        }
        generatedCode.append(" {").append(LINE_SEPARATOR)
                .append(DOUBLE_INDENT).append(code).append(LINE_SEPARATOR)
                .append(INDENT).append("}").append(DOUBLE_LINE_SEPARATOR);
    }

    /**
     * Create a string that definitely matches the {@code method}.
     *
     * @param method some method
     * @return string with name of {@code method} and all its parameters
     * @see Parameter
     */
    private String transformToString(Method method) {
        StringBuilder stringBuilder = new StringBuilder(method.getName());
        for (Parameter parameter : method.getParameters()) {
            stringBuilder.append(" ").append(parameter.getType().getCanonicalName());
        }
        return stringBuilder.toString();
    }

    /**
     * Check if {@code clazz} is utility class.
     * <p>
     * Check if {@code clazz} is concrete and contains only private constructors.
     * </p>
     *
     * @param clazz some class
     * @return {@code true} if {@code clazz} is utility class and {@code false} is it is not
     */
    private boolean isUtilityClass(Class clazz) {
        boolean s = Arrays.stream(clazz.getDeclaredConstructors()).allMatch(constructor -> Modifier.isPrivate(constructor.getModifiers()));
        return !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface() && s;
    }

    /**
     * Normalize modifiers.
     * <p>
     * It removes {@code ABSTRACT}, {@code INTERFACE}, {@code TRANSIENT} and {@code VOLATILE}
     * flags in {@code modifiers} if it contains them and transform it to {@link String}.
     * </p>
     *
     * @param modifiers a result of method {@code getModifiers()} for a class or a member
     * @return normalized {@code modifiers}.
     * @see Modifier
     */
    private String normalizeModifiers(int modifiers) {
        return Modifier.toString(modifiers & MODIFIERS_NORMALIZER);
    }

    /**
     * Create a string that contains modified name of the {@code clazz}.
     *
     * @param clazz  some class
     * @param extension extension of file of class
     * @return string with name of {@code class} with suffixes "Impl" {@code extension}
     */
    private String modifyClassName(Class clazz, String extension) {
        return clazz.getSimpleName() + "Impl" + extension;
    }

    /**
     * Create a manifest and return it.
     *
     * @return created manifest
     * @see Manifest
     */
    private Manifest createManifest() {
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.put(Attributes.Name.MANIFEST_VERSION, MANIFEST_VERSION);
        attributes.put(Attributes.Name.IMPLEMENTATION_VENDOR, AUTHOR_NAME);
        return manifest;
    }

    /**
     * Transform international symbols from {@code stringBuilder} to Unicode
     *
     * @param generatedCode {@link StringBuilder} of generated code
     * @return string builder in unicode format
     */
    private StringBuilder transformToUnicode(StringBuilder generatedCode) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < generatedCode.length(); i++) {
            char ch = generatedCode.charAt(i);
            if (ch >= 128) {
                unicode.append("\\u").append(String.format("%04X", (int) ch));
            } else {
                unicode.append(ch);
            }
        }
        return unicode;
    }

    /**
     * Write how to run {@code main(String[] args)} correctly
     */
    private static void getUsage() {
        System.out.println("Usage:");
        System.out.println(INDENT + "[class to implement] [path to save file]");
        System.out.println(INDENT + "-jar [class to implement] [path to save jar-file]");
    }

}