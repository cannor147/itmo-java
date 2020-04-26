package ru.ifmo.rain.bashunov.mapper.parallelism;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;
import ru.ifmo.rain.bashunov.concurrent.IterativeParallelism;

@SuppressWarnings("unused")
public class MappedParallelism extends IterativeParallelism {

    public MappedParallelism(ParallelMapper mapper) {
        threader = new MappedThreader(mapper);
    }

}
