package ru.ifmo.rain.bashunov.mapper;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;
import ru.ifmo.rain.bashunov.mapper.utils.SmartCounter;
import ru.ifmo.rain.bashunov.mapper.utils.Task;

import java.util.*;
import java.util.function.Function;

import static java.lang.Thread.interrupted;

@SuppressWarnings("unused")
public class ParallelMapperImpl implements ParallelMapper {

    private final List<Thread> threads;
    private final Queue<Task> tasks;

    public ParallelMapperImpl(int threads) {
        System.out.println(threads);
        this.threads = new ArrayList<>();
        this.tasks = new ArrayDeque<>();

        for (int i = 0; i < threads; i++) {
            Thread thread = new Thread(() -> {
                try {
                    while (!interrupted()) {
                        solveSomeTask();
                    }
                } catch (InterruptedException ignored) {
                    // ignored
                }
            });
            this.threads.add(thread);
            thread.start();
        }
    }

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> function, List<? extends T> list) throws InterruptedException {
        int size = list.size();
        List<R> result = new ArrayList<>(Collections.nCopies(size, null));
        SmartCounter counter = new SmartCounter(0);
        for (int i = 0; i < size; i++) {
            int index = i;
            Task task = new Task(() -> result.set(index, function.apply(list.get(index))), counter);
            addTaskToQueue(task);
        }

        counter.waitForValue(size);
        return result;
    }

    @SuppressWarnings("unused")
    public <T, R> R map(Function<? super T, ? extends R> function, T element) throws InterruptedException {
        List<R> result = new ArrayList<>(Collections.singletonList(null));
        SmartCounter counter = new SmartCounter(0);
        Task task = new Task(() -> result.set(0, function.apply(element)), counter);
        addTaskToQueue(task);

        counter.waitForValue(1);
        return result.get(0);
    }

    @Override
    public void close() {
        for (Thread thread : threads) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException ignored) {
                // ignored
            }
        }
    }

    private void addTaskToQueue(Task task) {
        synchronized (tasks) {
            tasks.add(task);
            tasks.notify();
        }
    }

    private void solveSomeTask() throws InterruptedException {
        Task task;
        synchronized (tasks) {
            while (tasks.isEmpty()) {
                tasks.wait();
            }
            task = tasks.poll();
        }

        task.run();
    }
}
