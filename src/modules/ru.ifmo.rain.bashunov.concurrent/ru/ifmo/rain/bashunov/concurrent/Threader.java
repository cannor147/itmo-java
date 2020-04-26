package ru.ifmo.rain.bashunov.concurrent;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

public abstract class Threader {

    /**
     * Transform list of elements to something else
     *
     * Split list of elements to some sublists and transform these sublists
     * to new type {@code <R>}. Then merge generated stream to one element.
     *
     * @param threads number of threads
     * @param list list of elements for solving
     * @param function function that transforms stream of elements in list to new type {@code <R>}
     * @param resultGrabber function to merge results of {@code function} to {@code <R>}
     * @param <T> type of elements in list
     * @param <R> result type
     *
     * @return transformed {@code list}
     *
     * @throws InterruptedException if executing thread was interrupted.
     */
    public abstract <T, R> R solve(int threads, List<? extends T> list, Function<Stream<? extends T>, R> function, Function<Stream<? extends R>, R> resultGrabber) throws InterruptedException;

    /**
     * Transform list of elements to one element
     *
     * Split list of elements to some sublists and transform these sublists
     * to one element. Then merge generated stream to one element again.
     *
     * @param threads number of threads
     * @param list list of elements for solving
     * @param function function that transforms stream of elements in list to new type {@code <R>}
     * @param <T> type of elements in list
     *
     * @return transformed {@code list}
     *
     * @throws InterruptedException if executing thread was interrupted.
     */
    @SuppressWarnings("WeakerAccess")
    public final <T> T solve(int threads, List<? extends T> list, Function<Stream<? extends T>, T> function) throws InterruptedException {
        return solve(threads, list, function, function);
    }

    /**
     * Transform list of elements to some collection
     *
     * Split list of elements to some sublists and transform these sublists
     * to one element. Then merge generated stream to new collection.
     *
     * @param threads number of threads
     * @param list list of elements for solving
     * @param function function that transforms stream of elements in list to new type {@code <R>}
     * @param collector collector to collect results of transforming sublists
     * @param <T> type of elements in list
     * @param <R> result type of transforming
     * @param <C> result type of collecting
     *
     * @return transformed {@code list} collected by {@code collector}
     *
     * @throws InterruptedException if executing thread was interrupted.
     */
    @SuppressWarnings("WeakerAccess")
    public final <T, R, C> C solve(int threads, List<? extends T> list, Function<Stream<? extends T>, Stream<R>> function, Collector<R, ?, ? extends C> collector) throws InterruptedException {
        return solve(threads, list, function, stream -> stream.flatMap(Function.identity())).collect(collector);
    }
}
