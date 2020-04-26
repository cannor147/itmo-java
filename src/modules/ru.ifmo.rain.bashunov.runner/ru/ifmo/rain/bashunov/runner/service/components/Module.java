package ru.ifmo.rain.bashunov.runner.service.components;

import java.util.ArrayList;
import java.util.List;

import static ru.ifmo.rain.bashunov.runner.service.BasicService.SEPARATOR;

public abstract class Module {

    protected String name;
    protected List<String> classFiles;
    protected List<String> javaFiles;

    protected Module(String name) {
        this.name = name;
        this.classFiles = new ArrayList<>();
        this.javaFiles = new ArrayList<>();
    }

    public void addClassFile(String name) {
        classFiles.add(name);
    }

    public void addJavaFile(String name) {
        javaFiles.add(name);
    }

    public String getName() {
        return name;
    }

    public abstract String getProductPath();
    public abstract String getSourcePath();

    public List<String> getClassFiles() {
        List<String> result = new ArrayList<>();
        for (String str: classFiles) {
            result.add(name + "." + str);
        }
        return result;
    }

    public List<String> getJavaFiles() {
        List<String> result = new ArrayList<>();
        for (String str: javaFiles) {
            result.add(name.replace(".", SEPARATOR) + SEPARATOR + str + ".java");
        }
        return result;
    }
}
