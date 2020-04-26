package ru.ifmo.rain.bashunov.runner.service.components;

import static ru.ifmo.rain.bashunov.runner.PathService.*;
import static ru.ifmo.rain.bashunov.runner.service.BasicService.SEPARATOR;

public class TaskModule extends Module {

    public TaskModule(String name) {
        super(name);
    }

    @Override
    public String getProductPath() {
        return OUT_PATH + SEPARATOR + name;
    }

    @Override
    public String getSourcePath() {
        return SRC_PATH + SEPARATOR + name;
    }
}
