package ru.ifmo.rain.bashunov.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 */
class IterativeThreader extends Threader {

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

        List<Thread> threadList = new ArrayList<>();
        List<R> results = new ArrayList<>(Collections.nCopies(threads, null));

        int d = size / threads, m = size % threads;
        for (int i = 0, l, r = 0; i < threads; i++) {
            l = r;
            r = l + d + (m-- > 0 ? 1 : 0);
            int index = i;
            List<? extends T> s = list.subList(l, r);
            Thread thread = new Thread(() -> results.set(index, function.apply(s.stream())));
            thread.start();
            threadList.add(thread);
        }

        InterruptedException exception = null;
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                if (exception == null) {
                    exception = e;
                } else {
                    exception.addSuppressed(e);
                }
            }
        }
        if (exception != null) {
            throw exception;
        }

        return resultGrabber.apply(results.stream());
    }

    /**
     * Validate input arguments for correctness
     *
     * @param threads number of threads
     * @param list list of elements
     * @param <T> type of elements in list
     *
     * @throws IllegalArgumentException if {@code threads} less or equals zero
     */
    private <T> void validate(int threads, List<? extends T> list) throws IllegalArgumentException, NullPointerException {
        if (threads <= 0) throw new IllegalArgumentException("Number of thread should be more than zero");
        Objects.requireNonNull(list);
    }
}
