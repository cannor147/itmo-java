### Домашнее задание 8. Параллельный запуск

1. Напишите класс `ParallelMapperImpl`, реализующий интерфейс `ParallelMapper`.

   ```java
   public interface ParallelMapper extends AutoCloseable {
       <T, R> List<R> run(
           Function<? super T, ? extends R> f, 
           List<? extends T> args
       ) throws InterruptedException;
   
       @Override
       void close() throws InterruptedException;
   }
   ```

   - Метод `run` должен параллельно вычислять функцию `f` на каждом из указанных аргументов (`args`).
   - Метод `close` должен останавливать все рабочие потоки.
   - Конструктор `ParallelMapperImpl(int threads)` создает `threads` рабочих потоков, которые могут быть использованы для распараллеливания.
   - К одному `ParallelMapperImpl` могут одновременно обращаться несколько клиентов.
   - Задания на исполнение должны накапливаться в очереди и обрабатываться в порядке поступления.
   - В реализации не должно быть активных ожиданий.

2. Модифицируйте касс `IterativeParallelism` так, чтобы он мог использовать `ParallelMapper`.

   - Добавьте конструктор `IterativeParallelism(ParallelMapper)`
   - Методы класса должны делить работу на `threads` фрагментов и исполнять их при помощи `ParallelMapper`.
   - Должна быть возможность одновременного запуска и работы нескольких клиентов, использующих один `ParallelMapper`.
   - При наличии `ParallelMapper` сам `IterativeParallelism` новые потоки создавать не должен.

