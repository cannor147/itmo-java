package ru.ifmo.rain.bashunov.runner;

import ru.ifmo.rain.bashunov.runner.service.components.Module;
import ru.ifmo.rain.bashunov.runner.service.components.TestModule;
import ru.ifmo.rain.bashunov.runner.service.configurations.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.*;

import static ru.ifmo.rain.bashunov.runner.PathService.*;
import static ru.ifmo.rain.bashunov.runner.service.BasicService.*;

public class HomeworkRunner {

    public static void main(String[] args) {
        HomeworkRunner homeworkRunner = new HomeworkRunner();

        File tempDir = new File(TEMP_PATH);
        if (!tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        Configuration configuration;
        try {
            configuration = Configurations.getConfiguration(args);
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            writeErrorMessage("Your arguments are incorrect");
            usage();
            return;
        }
        if (configuration.getName().equals("run")) {
            String answer, salt;
            Scanner scanner = new Scanner(System.in);
            System.out.print("Do you want to add the salt? [Y/n] ");
            answer = scanner.next();
            if (answer.toLowerCase().equals("y")) {
                System.out.print("Write the salt: ");
                salt = scanner.next();
                homeworkRunner.run((RunConfiguration) configuration, salt);
            } else {
                homeworkRunner.run((RunConfiguration) configuration);
            }
        } else if (configuration.getName().equals("javadoc")) {
            homeworkRunner.generateJavadoc((JavadocConfiguration) configuration);
        } else if (configuration.getName().equals("execute")) {
            homeworkRunner.executeCommand((ExecuteConfiguration) configuration);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void run(RunConfiguration configuration) {
        List<Module> dependencies = new ArrayList<>(configuration.getModules());
        TestModule testModule = configuration.getTestModule();
        dependencies.add(testModule);
        String cp = getClassPath(dependencies);

        String modification = configuration.getModification();
        String[] args = new String[]{"java", "-cp", cp, "-p", cp, "-m", testModule.getName(), modification, configuration.getClassFilesWithCommo()};
        executeCommand(new ExecuteConfiguration(args));
    }

    @SuppressWarnings("WeakerAccess")
    public void run(RunConfiguration configuration, String salt) {
        List<Module> dependencies = new ArrayList<>(configuration.getModules());
        TestModule testModule = configuration.getTestModule();
        dependencies.add(testModule);
        String cp = getClassPath(dependencies);

        String modification = configuration.getModification();
        String[] args = new String[]{"java", "-cp", cp, "-p", cp, "-m", testModule.getName(), modification, configuration.getClassFilesWithCommo(), salt};
        executeCommand(new ExecuteConfiguration(args));
    }

    @SuppressWarnings("WeakerAccess")
    public void generateJavadoc(JavadocConfiguration configuration) {
        String cp = getClassPath(configuration.getDependencies());
        String module = configuration.getModule().getName();
        for (String javaFile : configuration.getJavaFiles()) {
            String file = configuration.getModule().getSourcePath() + SEPARATOR + javaFile;
            String[] args = new String[]{"javadoc", "-cp", cp, "-d", getJavadocPath(), "-private", "-link", getOraclePath(), file};
            executeCommand(new ExecuteConfiguration(args));
            System.out.println(LINE_SEPARATOR + "Link:" + LINE_SEPARATOR + javadocHtml(javaFile));
            System.out.println();
        }
    }

//    private void executeCommand(final String[] command) {
    private void executeCommand(ExecuteConfiguration configuration) {
        StringBuilder commandLine = new StringBuilder();
        for (String s1 : configuration.getArguments()) {
            commandLine.append(s1).append(" ");
        }
        System.out.println(commandLine.toString());
        try {
            Process p = Runtime.getRuntime().exec(configuration.getArguments());

            String s;
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            writeErrorMessage("You have bad command line arguments");
            e.printStackTrace();
        } catch (SecurityException e) {
            writeErrorMessage("Security error");
            e.printStackTrace();
        }
    }

    /**
     * Write how to run {@code main(String[] args)} correctly
     */
    private static void usage() {
        System.out.println("Usage:");
        System.out.println(TAB + "-run --test [test_module] --mod [modification] --module [your_module] --classes [your_classes]");
        System.out.println(TAB + "-javadoc --test [test_module] --module [your_module] --sources [your_sources]");
        System.out.println(TAB + "-command [args]");
    }
}
