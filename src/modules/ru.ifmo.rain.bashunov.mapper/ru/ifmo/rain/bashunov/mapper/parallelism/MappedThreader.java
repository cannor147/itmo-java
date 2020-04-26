package ru.ifmo.rain.bashunov.mapper.parallelism;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;
import ru.ifmo.rain.bashunov.concurrent.Threader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class MappedThreader extends Threader {

    private ParallelMapper mapper;

    @SuppressWarnings("WeakerAccess")
    public MappedThreader(ParallelMapper mapper) {
        this.mapper = mapper;
    }

    /**
     *
     * @param threads number of threads
     * @param list list of elements for solving
     * @param function function that transforms stream of elements in list to new type {@code <R>}
     * @param resultGrabber function to merge results of {@code mapper} to {@code <R>}
     * @param <T> type of elements in list
     * @param <R> result type
     *
     * @return transformed {@code list}
     *
     * @throws InterruptedException if executing thread was interrupted.
     */
    @Override
    public <T, R> R solve(int threads, List<? extends T> list, Function<Stream<? extends T>, R> function, Function<Stream<? extends R>, R> resultGrabber) throws InterruptedException {
        validate(threads, list);
        int size = list.size();
        threads = Math.max(1, Math.min(threads, size));

        List<List<? extends T>> tasks = new ArrayList<>();

        int d = size / threads, m = size % threads;
        for (int i = 0, l, r = 0; i < threads; i++) {
            l = r;
            r = l + d + (m-- > 0 ? 1 : 0);
            tasks.add(list.subList(l, r));
        }

        return resultGrabber.apply(mapper.map((task) -> function.apply(task.stream()), tasks).stream());
    }

    private <T> void validate(int threads, List<? extends T> list) throws IllegalArgumentException {
        if (threads <= 0) throw new IllegalArgumentException("Number of thread should be more than zero");
        Objects.requireNonNull(list);
    }
}
