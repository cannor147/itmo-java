package ru.ifmo.rain.bashunov.hello;

public class BasicService {


    /**
     * Check if number of threads isn't less than zero
     *
     * @param threads number of threads
     */
    static void validate(int threads) throws IllegalArgumentException {
        if (threads <= 0) {
            throw new IllegalArgumentException("Number of thread should be more than zero");
        }
    }

    /**
     * Just write the error message.
     *
     * @param errorMessage information about error
     */
    static void writeErrorMessage(String errorMessage) {
        System.err.println(errorMessage);
    }
}
