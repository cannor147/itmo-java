### Домашнее задание 1. Обход файлов

1. Разработайте класс `Walk`, осуществляющий подсчет хеш-сумм файлов.

   1. Формат запуска

      ```
      java Walk <входной файл> <выходной файл>
      ```

   2. Входной файл содержит список файлов, которые требуется обойти.

   3. Выходной файл должен содержать по одной строке для каждого файла. Формат строки:

      ```
      <шестнадцатеричная хеш-сумма> <путь к файлу>
      ```

   4. Для подсчета хеш-суммы используйте алгоритм [FNV](https://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function).

   5. Если при чтении файла возникают ошибки, укажите в качестве его хеш-суммы `00000000`.

   6. Кодировка входного и выходного файлов — UTF-8.

   7. Если родительская директория выходного файла не существует, то соответствующий путь надо создать.

   8. Размеры файлов могут превышать размер оперативной памяти.

   9. Пример

      Входной файл

      ```
      java/info/kgeorgiy/java/advanced/walk/samples/1
      java/info/kgeorgiy/java/advanced/walk/samples/12
      java/info/kgeorgiy/java/advanced/walk/samples/123
      java/info/kgeorgiy/java/advanced/walk/samples/1234
      java/info/kgeorgiy/java/advanced/walk/samples/1
      java/info/kgeorgiy/java/advanced/walk/samples/binary
      java/info/kgeorgiy/java/advanced/walk/samples/no-such-file
      ```

      Выходной файл

      ```
      050c5d2e java/info/kgeorgiy/java/advanced/walk/samples/1
      2076af58 java/info/kgeorgiy/java/advanced/walk/samples/12
      72d607bb java/info/kgeorgiy/java/advanced/walk/samples/123
      81ee2b55 java/info/kgeorgiy/java/advanced/walk/samples/1234
      050c5d2e java/info/kgeorgiy/java/advanced/walk/samples/1
      8e8881c5 java/info/kgeorgiy/java/advanced/walk/samples/binary
      00000000 java/info/kgeorgiy/java/advanced/walk/samples/no-such-file
      ```

2. Усложненная версия:

   1. Разработайте класс `RecursiveWalk`, осуществляющий подсчет хеш-сумм файлов в директориях

   2. Входной файл содержит список файлов и директорий, которые требуется обойти. Обход директорий осуществляется рекурсивно.

   3. Пример

      Входной файл

      ```
      java/info/kgeorgiy/java/advanced/walk/samples/binary
      java/info/kgeorgiy/java/advanced/walk/samples
      ```

      Выходной файл

      ```
      8e8881c5 java/info/kgeorgiy/java/advanced/walk/samples/binary
      050c5d2e java/info/kgeorgiy/java/advanced/walk/samples/1
      2076af58 java/info/kgeorgiy/java/advanced/walk/samples/12
      72d607bb java/info/kgeorgiy/java/advanced/walk/samples/123
      81ee2b55 java/info/kgeorgiy/java/advanced/walk/samples/1234
      8e8881c5 java/info/kgeorgiy/java/advanced/walk/samples/binary
      ```

3. При выполнении задания следует обратить внимание на:

   - Дизайн и обработку исключений, диагностику ошибок.
   - Программа должна корректно завершаться даже в случае ошибки.
   - Корректная работа с вводом-выводом.
   - Отсутствие утечки ресурсов.

4. Требования к оформлению задания.

   - Проверяется исходный код задания.
   - Весь код должен находиться в пакете `ru.ifmo.rain.фамилия.walk`.