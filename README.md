[![en](https://img.shields.io/badge/lang-en-red.svg)](README.md) [![en](https://img.shields.io/badge/lang-ru-blue.svg)](README.ru.md)

# Java Advanced

Course was prepared and taught by [Georgiy Korneev](https://github.com/kgeorgiy) at ITMO University in 2019 (year2017, 4th semester).

## Project setting in IntelliJ IDEA

1. Open root directory of the repository. Unmark source mark from `src` directory for main project module if it is.
2. Do `Import module..` for each directory in `src/modules`. If you want only one specific module, do `Import module..` for:
    * module of your choice
    * dependent modules from your choice (see point 4)
    * module `ru.ifmo.rain.bashunov.runner`
3. Add directories `test/artifats` and `test/lib` as `Library` in your project. Also ass dependencies for each project modules to this libraries.
4. Add dependencies between modules:
    * `ru.ifmo.rain.bashunov.mapper` from `ru.ifmo.rain.bashunov.concurrent`
5. Before running the tests, run `Build` for the whole project. After any change you should do `Rebuild` for the whole project.
6. For running the tests you should use `HomeworkRunner` from module `ru.ifmo.rain.bashunov.runner`.

## Homeworks

1. File crawling ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.walk/tasks.md), [solution](src/modules/ru.ifmo.rain.bashunov.walk))
    * running tests for simple modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.walk --mod Walk --module ru.ifmo.rain.bashunov.walk --class RecursiveWalk`
    * running tests for advanced modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.walk --mod RecursiveWalk --module ru.ifmo.rain.bashunov.walk --class RecursiveWalk`
2. Set on array ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.arrayset/tasks.md), [solution](src/modules/ru.ifmo.rain.bashunov.arrayset))
    * running tests for simple modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.arrayset --mod SortedSet --module ru.ifmo.rain.bashunov.arrayset --class ArraySet`
    * running tests for advanced modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.arrayset --mod NavigableSet --module ru.ifmo.rain.bashunov.arrayset --class ArraySet`
3. Students ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.student/tasks.md), [solution](src/modules/ru.ifmo.rain.bashunov.student))
    * running tests for simple modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.student --mod StudentQuery --module ru.ifmo.rain.bashunov.student --class StudentQueryImpl`
    * running tests for advanced modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.student --mod StudentGroupQuery --module ru.ifmo.rain.bashunov.student --class StudentQueryImpl`
4. Implementor ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.implementor/tasks.md), [solution](src/modules/ru.ifmo.rain.bashunov.implementor))
    * running tests for simple modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.implementor --mod interface --module ru.ifmo.rain.bashunov.implementor --class Implementor`
    * running tests for advanced modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.implementor --mod class --module ru.ifmo.rain.bashunov.implementor --class Implementor`
5. Jar Implementor ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.implementor/tasks.md), [solution](src/modules/ru.ifmo.rain.bashunov.implementor))
    * running tests for simple modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.implementor --mod jar-interface --module ru.ifmo.rain.bashunov.implementor --class Implementor`
    * running tests for advanced modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.implementor --mod jar-class --module ru.ifmo.rain.bashunov.implementor --class Implementor`
6. Javadoc ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.implementor/tasks.md), [solution](src/modules/ru.ifmo.rain.bashunov.implementor))
    * script for creating Javadoc:
      `HomeworkRunner -javadoc --test info.kgeorgiy.java.advanced.implementor --module ru.ifmo.rain.bashunov.implementor --source Implementor`
7. Iterative parallelism ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.concurrent/tasks.md), [solution](src/modules/ru.ifmo.rain.bashunov.concurrent))
    * running tests for simple modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.concurrent --mod scalar --module ru.ifmo.rain.bashunov.concurrent --class IterativeParallelism`
    * running tests for advanced modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.concurrent --mod list --module ru.ifmo.rain.bashunov.concurrent --class IterativeParallelism`
8. Parallel run ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.mapper/tasks.md), [solution](src/modules/ru.ifmo.rain.bashunov.mapper))
    * running tests for simple modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.mapper --mod scalar --module ru.ifmo.rain.bashunov.concurrent --module ru.ifmo.rain.bashunov.mapper --class ParallelMapperImpl parallelism.MappedParallelism`
    * running tests for advanced modification:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.mapper --mod list --module ru.ifmo.rain.bashunov.concurrent --module ru.ifmo.rain.bashunov.mapper --class ParallelMapperImpl parallelism.MappedParallelism`
9. Web Crawler ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.crawler/tasks.md), [solution draft](src/modules/ru.ifmo.rain.bashunov.crawler))
10. HelloUDP ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.hello/tasks.md), [solution draft](src/modules/ru.ifmo.rain.bashunov.hello))
11. HelloUDP ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.bank/tasks.md))
12. Text statistics ([statements (ru)](src/modules/ru.ifmo.rain.bashunov.i18n/tasks.md))

## Useful links

* [Course page "Java Advanced" on the site of G. Korneev (ru)](http://www.kgeorgiy.info/courses/java-advanced/)
