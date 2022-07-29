[![en](https://img.shields.io/badge/lang-en-red.svg)](README.md) [![en](https://img.shields.io/badge/lang-ru-blue.svg)](README.ru.md)

# Java Advanced

Курс подготовил и прочитал [Георгий Корнеев](https://github.com/kgeorgiy) в Университете ИТМО в 2019 году (year2017, 4 семестр).

## Настройка проекта в IntelliJ IDEA

1. Откройте корневую директорию репозитория. Снимите отметку с папки `src`, что данная директория содержит sources для главного модуля проекта, если такая отметка присутствует.
2. Сделайте `Import module..` для каждой папки в `src/modules`. Если вас интересует только конкретный модуль, тогда сделайте `Import module..` для:
    * выбранного вами модуля
    * модулей, от которых зависит выбранный вами модуль (см. пункт 4)
    * модуля `ru.ifmo.rain.bashunov.runner`
3. Добавьте папки `test/artifats` и `test/lib` в качестве `Library` в вашем проекте. Добавьте также зависимости для всех модулей проекта на эти библиотеки.
4. Добавьте зависимости между модулями:
    * `ru.ifmo.rain.bashunov.mapper` от `ru.ifmo.rain.bashunov.concurrent`
5. Прежде чем запускать тесты, запустите `Build` для всего проекта. При любом изменении необходимо делать `Rebuild` для всего проекта.
6. Для запуска тестов необходимо использовать `HomeworkRunner` из модуля `ru.ifmo.rain.bashunov.runner`.

## Домашние работы

1. Обход файлов ([условия](src/modules/ru.ifmo.rain.bashunov.walk/tasks.md), [решение](src/modules/ru.ifmo.rain.bashunov.walk))
    * запуск тестов для простой модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.walk --mod Walk --module ru.ifmo.rain.bashunov.walk --class RecursiveWalk`
    * запуск тестов для сложной модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.walk --mod RecursiveWalk --module ru.ifmo.rain.bashunov.walk --class RecursiveWalk`
2. Множество на массиве ([условия](src/modules/ru.ifmo.rain.bashunov.arrayset/tasks.md), [решение](src/modules/ru.ifmo.rain.bashunov.arrayset))
    * запуск тестов для простой модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.arrayset --mod SortedSet --module ru.ifmo.rain.bashunov.arrayset --class ArraySet`
    * запуск тестов для сложной модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.arrayset --mod NavigableSet --module ru.ifmo.rain.bashunov.arrayset --class ArraySet`
3. Студенты ([условия](src/modules/ru.ifmo.rain.bashunov.student/tasks.md), [решение](src/modules/ru.ifmo.rain.bashunov.student))
    * запуск тестов для простой модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.student --mod StudentQuery --module ru.ifmo.rain.bashunov.student --class StudentQueryImpl`
    * запуск тестов для сложной модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.student --mod StudentGroupQuery --module ru.ifmo.rain.bashunov.student --class StudentQueryImpl`
4. Implementor ([условия](src/modules/ru.ifmo.rain.bashunov.implementor/tasks.md), [решение](src/modules/ru.ifmo.rain.bashunov.implementor))
    * запуск тестов для простой модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.implementor --mod interface --module ru.ifmo.rain.bashunov.implementor --class Implementor`
    * запуск тестов для сложной модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.implementor --mod class --module ru.ifmo.rain.bashunov.implementor --class Implementor`
5. Jar Implementor ([условия](src/modules/ru.ifmo.rain.bashunov.implementor/tasks.md), [решение](src/modules/ru.ifmo.rain.bashunov.implementor))
    * запуск тестов для простой модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.implementor --mod jar-interface --module ru.ifmo.rain.bashunov.implementor --class Implementor`
    * запуск тестов для сложной модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.implementor --mod jar-class --module ru.ifmo.rain.bashunov.implementor --class Implementor`
6. Javadoc ([условия](src/modules/ru.ifmo.rain.bashunov.implementor/tasks.md), [решение](src/modules/ru.ifmo.rain.bashunov.implementor))
    * скрипт для создания Javadoc:
      `HomeworkRunner -javadoc --test info.kgeorgiy.java.advanced.implementor --module ru.ifmo.rain.bashunov.implementor --source Implementor`
7. Итеративный параллелизм ([условия](src/modules/ru.ifmo.rain.bashunov.concurrent/tasks.md), [решение](src/modules/ru.ifmo.rain.bashunov.concurrent))
    * запуск тестов для простой модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.concurrent --mod scalar --module ru.ifmo.rain.bashunov.concurrent --class IterativeParallelism`
    * запуск тестов для сложной модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.concurrent --mod list --module ru.ifmo.rain.bashunov.concurrent --class IterativeParallelism`
8. Параллельный запуск ([условия](src/modules/ru.ifmo.rain.bashunov.mapper/tasks.md), [решение](src/modules/ru.ifmo.rain.bashunov.mapper))
    * запуск тестов для простой модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.mapper --mod scalar --module ru.ifmo.rain.bashunov.concurrent --module ru.ifmo.rain.bashunov.mapper --class ParallelMapperImpl parallelism.MappedParallelism`
    * запуск тестов для сложной модификации:
      `HomeworkRunner -run --test info.kgeorgiy.java.advanced.mapper --mod list --module ru.ifmo.rain.bashunov.concurrent --module ru.ifmo.rain.bashunov.mapper --class ParallelMapperImpl parallelism.MappedParallelism`
9. Web Crawler ([условия](src/modules/ru.ifmo.rain.bashunov.crawler/tasks.md), [черновик решения](src/modules/ru.ifmo.rain.bashunov.crawler))
10. HelloUDP ([условия](src/modules/ru.ifmo.rain.bashunov.hello/tasks.md), [черновик решения](src/modules/ru.ifmo.rain.bashunov.hello))
11. HelloUDP ([условия](src/modules/ru.ifmo.rain.bashunov.bank/tasks.md))
12. Статистика текста ([условия](src/modules/ru.ifmo.rain.bashunov.i18n/tasks.md))

## Полезные ссылки

* [Страница курса "Java Advanced" на сайте Г.А. Корнеева](http://www.kgeorgiy.info/courses/java-advanced/)
