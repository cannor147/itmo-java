package ru.ifmo.rain.bashunov.runner.service.configurations;

public class ExecuteConfiguration implements Configuration {

    private String[] arguments;

    public ExecuteConfiguration(String[] args) {
        arguments = args;
    }

    @Override
    public String getName() {
        return "execute";
    }

    public String[] getArguments() {
        return arguments;
    }
}
