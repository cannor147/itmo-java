package ru.ifmo.rain.bashunov.runner.service.configurations;

import ru.ifmo.rain.bashunov.runner.service.components.TaskModule;
import ru.ifmo.rain.bashunov.runner.service.components.TestModule;

import java.util.Arrays;
import java.util.Objects;

/**
 * The type Configurations.
 */
public class Configurations {

    public static Configuration getConfiguration(String[] args) throws IllegalArgumentException {
        if (args == null || Arrays.stream(args).anyMatch(Objects::isNull)) {
            throw new NullPointerException();
        }
        switch (args[0]) {
            case "-run":
            case "-r":
                return getRunConfiguration(args);
            case "-javadoc":
            case "-d":
                return getJavadocConfiguration(args);
            case "-compile":
            case "-c":
                return getExecuteConfiguration(args);
        }

        throw new IllegalArgumentException();
    }

    private static RunConfiguration getRunConfiguration(String[] args) throws IllegalArgumentException {
        RunConfiguration config = new RunConfiguration();
        int i = 1;
        boolean tests = false, modification = false;
        while (i < args.length) {
            if (i == args.length - 1) {
                throw new IllegalArgumentException();
            }
            switch (args[i]) {
                case "--tests":
                case "--test":
                    if (tests) throw new IllegalArgumentException();
                    i++;
                    config.setTestModule(new TestModule(args[i]));
                    tests = true;
                    break;
                case "--modification":
                case "--mod":
                    if (modification) throw new IllegalArgumentException();
                    i++;
                    config.setModification(args[i]);
                    modification = true;
                    break;
                case "--module":
                case "--m":
                    if (!tests && !modification) throw new IllegalArgumentException();
                    i++;
                    config.addModule(new TaskModule(args[i]));
                    break;
                case "--module-tests":
                case "--module-test":
                case "--m-tests":
                case "--m-test":
                    if (!tests && !modification) throw new IllegalArgumentException();
                    i++;
                    config.addModule(new TestModule(args[i]));
                    break;
                case "--classes":
                case "--class":
                case "--c":
                    while (i + 1 < args.length && !isCommand(args[i + 1])) {
                        i++;
                        config.addClassFile(args[i]);
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            i++;
        }
        if (!tests || !modification || config.getModules().isEmpty() || config.getClassFiles().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return config;
    }

    private static JavadocConfiguration getJavadocConfiguration(String[] args) throws IllegalArgumentException {
        JavadocConfiguration config = new JavadocConfiguration();
        int i = 1;
        boolean module = false;
        while (i < args.length) {
            if (i == args.length - 1) {
                throw new IllegalArgumentException();
            }
            switch (args[i]) {
                case "--test":
                case "--tests":
                    i++;
                    config.addDependencyModule(new TestModule(args[i]));
                    break;
                case "--dependency":
                case "--d":
                    i++;
                    config.addDependencyModule(new TaskModule(args[i]));
                    break;
                case "--module":
                case "--m":
                    i++;
                    config.setModule(args[i]);
                    module = true;
                    break;
                case "--sources":
                case "--source":
                case "--src":
                case "--j":
                    while (i + 1 < args.length && !isCommand(args[i + 1])) {
                        i++;
                        config.addJavaFile(args[i]);
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            i++;
        }
        if (!module || config.getJavaFiles().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return config;
    }

    private static ExecuteConfiguration getExecuteConfiguration(String[] args) {
        if (args.length == 1) throw new IllegalArgumentException();
        return new ExecuteConfiguration(Arrays.copyOfRange(args, 1, args.length));
    }

    private static boolean isCommand(String text) {
        return text.startsWith("--");
    }
}
