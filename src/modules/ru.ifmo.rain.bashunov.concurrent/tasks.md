### Домашнее задание 7. Итеративный параллелизм

1. Реализуйте класс `IterativeParallelism`, который будет обрабатывать списки в несколько потоков.
2. В _простом_ варианте должны быть реализованы следующие методы:
   - `minimum(threads, list, comparator)` — первый минимум;
   - `maximum(threads, list, comparator)` — первый максимум;
   - `all(threads, list, predicate)` — проверка, что все элементы списка удовлетворяют [предикату](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html);
   - `any(threads, list, predicate)` — проверка, что существует элемент списка, удовлетворяющий [предикату](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html).
3. В _сложном_ варианте должны быть дополнительно реализованы следующие методы:
   - `filter(threads, list, predicate)` — вернуть список, содержащий элементы удовлетворяющие [предикату](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html);
   - `map(threads, list, function)` — вернуть список, содержащий результаты применения [функции](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html);
   - `join(threads, list)` — конкатенация строковых представлений элементов списка.
4. Во все функции передается параметр `threads` — сколько потоков надо использовать при вычислении. Вы можете рассчитывать, что число потоков не велико.
5. Не следует рассчитывать на то, что переданные компараторы, предикаты и функции работают быстро.
6. При выполнении задания нельзя использовать *Concurrency Utilities*.
7. Рекомендуется подумать, какое отношение к заданию имеют [моноиды](https://en.wikipedia.org/wiki/Monoid).