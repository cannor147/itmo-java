package ru.ifmo.rain.bashunov.crawler;

import info.kgeorgiy.java.advanced.crawler.Document;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class MyQueue<S, T> {

    private List<Level<S, T>> lol;
    private Map<S, Integer> added;

    public void add(S s, T t, Integer level) {
        if (level > lol.size()) throw new IllegalArgumentException("Level cannot be more than upper bound");
        synchronized (added) {
            if (added.containsKey(s)) {
                if (added.get(s) > level) {
                    synchronized (lol.get(added.get(s))) {
                        lol.get(added.get(s)).remove(s);
                        synchronized (lol.get(level)) {
                            lol.get(level).put(s, t);
                        }
                    }
                }
            }
        }
    }


    private class Level<E, D> extends HashMap<E, D> {

        private Integer level;

        /**
         * Constructs an empty {@code HashMap} with the specified initial
         * capacity and load factor.
         *
         * @param initialCapacity the initial capacity
         * @param loadFactor      the load factor
         * @throws IllegalArgumentException if the initial capacity is negative
         *                                  or the load factor is nonpositive
         */
        public Level(int initialCapacity, float loadFactor, Integer level) {
            super(initialCapacity, loadFactor);
            this.level = level;
        }

        /**
         * Constructs an empty {@code HashMap} with the specified initial
         * capacity and the default load factor (0.75).
         *
         * @param initialCapacity the initial capacity.
         * @throws IllegalArgumentException if the initial capacity is negative.
         */
        public Level(int initialCapacity, Integer level) {
            super(initialCapacity);
            this.level = level;
        }

        /**
         * Constructs an empty {@code HashMap} with the default initial capacity
         * (16) and the default load factor (0.75).
         */
        public Level(Integer level) {
            this.level = level;
        }

        /**
         * Constructs a new {@code HashMap} with the same mappings as the
         * specified {@code Map}.  The {@code HashMap} is created with
         * default load factor (0.75) and an initial capacity sufficient to
         * hold the mappings in the specified {@code Map}.
         *
         * @param m the map whose mappings are to be placed in this map
         * @throws NullPointerException if the specified map is null
         */
        public Level(Map<? extends E, ? extends D> m, Integer level) {
            super(m);
            this.level = level;
        }
    }
}
