package ru.ifmo.rain.bashunov.runner.service.configurations;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.rain.bashunov.runner.service.components.Module;
import ru.ifmo.rain.bashunov.runner.service.components.TestModule;

public class RunConfiguration implements Configuration {

    private TestModule testModule;
    private String modification;
    private List<Module> modules;

    RunConfiguration() {
        modules = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "run";
    }

    public TestModule getTestModule() {
        return testModule;
    }

    public String getModification() {
        return modification;
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<String> getClassFiles() {
        List<String> result = new ArrayList<>();
        for (Module module : modules) {
            result.addAll(module.getClassFiles());
        }
        return result;
    }

    public String getClassFilesWithCommo() {
        List<String> tmp = getClassFiles();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tmp.size(); i++) {
            if (i > 0) result.append(",");
            result.append(tmp.get(i));
        }
        return result.toString();
    }

    void addModule(Module module) {
        modules.add(module);
    }

    void addClassFile(String name) throws NullPointerException {
        if (modules.size() == 0) throw new NullPointerException();
        modules.get(modules.size() - 1).addClassFile(name);
    }

    void setModification(String modification) {
        this.modification = modification;
    }

    void setTestModule(TestModule testModule) {
        this.testModule = testModule;
    }
}
