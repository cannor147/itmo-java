package ru.ifmo.rain.bashunov.mapper.utils;

public class Task implements Runnable {

    private Runnable work;
    private boolean finished;
    private SmartCounter counter;

    @SuppressWarnings("unused")
    public Task(Runnable work) {
        this.work = work;
        this.finished = false;
        this.counter = new SmartCounter(0);
    }

    public Task(Runnable work, SmartCounter counter) {
        this.work = work;
        this.finished = false;
        this.counter = counter;
    }

    @Override
    public void run() {
        work.run();
        finished = true;
        counter.increment();
    }

    @SuppressWarnings("unused")
    public boolean isFinished() {
        return finished;
    }
}
