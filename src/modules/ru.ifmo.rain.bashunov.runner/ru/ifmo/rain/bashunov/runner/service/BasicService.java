package ru.ifmo.rain.bashunov.runner.service;

import java.io.File;

public class BasicService {

    public static final String ENCODING = "UTF8";

    public static final String TAB = "\t";
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String PATH_SEPARATOR = File.pathSeparator;
    public static final String SEPARATOR = File.separator;

    public static final String D_TAB = TAB + TAB;
    public static final String D_LINE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;

    /**
     * Just write the error message.
     *
     * @param errorMessage information about error
     */
    public static void writeErrorMessage(String errorMessage) {
        System.err.println(errorMessage);
    }
}
