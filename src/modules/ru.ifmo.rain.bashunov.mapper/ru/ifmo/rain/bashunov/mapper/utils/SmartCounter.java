package ru.ifmo.rain.bashunov.mapper.utils;

public class SmartCounter {

    private int value;
    private final SmartCounter parent;

    private SmartCounter(int value, SmartCounter parent) {
        this.value = value;
        this.parent = parent;
    }

    public SmartCounter(int value) {
        this(value, null);
    }

    public SmartCounter() {
        this(0, null);
    }

    public SmartCounter createChild() {
        return new SmartCounter(0, this);
    }

    @SuppressWarnings("WeakerAccess")
    public synchronized void increment() {
        value++;
        notify();
        if (parent != null) {
            parent.increment();
        }
    }

    @SuppressWarnings("unused")
    public synchronized void decrement() {
        value--;
        notify();
        if (parent != null) {
            parent.decrement();
        }
    }

    @SuppressWarnings("unused")
    public synchronized int getValue() {
        int result = value;
        notify();
        return result;
    }

    public synchronized void waitForValue(int value) throws InterruptedException {
        while (this.value != value) {
            this.wait();
        }
        notify();
    }

    public synchronized void waitUntilValue(int value) throws InterruptedException {
        while (this.value == value) {
            this.wait();
        }
        notify();
    }

    public synchronized void waitUntilChange() throws InterruptedException {
        int value = this.value;
        while (this.value == value) {
            this.wait();
        }
        notify();
    }

    public synchronized void incrementWithUpperBound(int upperBound) throws InterruptedException {
        while (value >= upperBound) {
            this.wait();
        }
        value++;
        notify();
        if (parent != null) {
            parent.increment();
        }
    }

}
