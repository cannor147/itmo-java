package ru.ifmo.rain.bashunov.runner.service.components;

import static ru.ifmo.rain.bashunov.runner.PathService.*;
import static ru.ifmo.rain.bashunov.runner.service.BasicService.*;

public class TestModule extends Module {

    public TestModule(String name) {
        super(name);
    }

    @Override
    public String getProductPath() {
        return ARTIFACTS_PATH + SEPARATOR + name + ".jar";
    }

    @Override
    public String getSourcePath() {
        return MODULES_PATH + SEPARATOR + name;
    }

}
