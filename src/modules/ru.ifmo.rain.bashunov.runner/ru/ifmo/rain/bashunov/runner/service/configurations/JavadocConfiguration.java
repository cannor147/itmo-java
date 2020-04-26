package ru.ifmo.rain.bashunov.runner.service.configurations;

import ru.ifmo.rain.bashunov.runner.service.components.Module;
import ru.ifmo.rain.bashunov.runner.service.components.TaskModule;

import java.util.ArrayList;
import java.util.List;

public class JavadocConfiguration implements Configuration {

    private List<Module> dependencyModules;
    private TaskModule module;

    JavadocConfiguration() {
        dependencyModules = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "javadoc";
    }

    public List<Module> getDependencies() {
        List<Module> result = new ArrayList<>(dependencyModules);
        result.add(module);
        return result;
    }

    public Module getModule() {
        return module;
    }

    public List<String> getJavaFiles() {
        return module.getJavaFiles();
    }

    void addDependencyModule(Module module) {
        dependencyModules.add(module);
    }

    void setModule(String name) {
        module = new TaskModule(name);
    }

    void addJavaFile(String name) {
        if (module == null) throw new NullPointerException();
        module.addJavaFile(name);
    }
}
