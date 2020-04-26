package ru.ifmo.rain.bashunov.implementor;

import java.io.File;

final class BasicService {

    /**
     * Standard encoding
     */
    static final String ENCODING = "UTF8";

    /**
     * Name of author of JAR file
     */
    static final String AUTHOR_NAME = "Erofey Bashunov";

    /**
     * Version of manifests for JAR file
     */
    static final String MANIFEST_VERSION = "1.0";

    /**
     * Simple indent
     */
    static final String INDENT = "\t";

    /**
     * Simple line separator
     */
    static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * Simple path separator
     * @see File
     */
    static final String PATH_SEPARATOR = File.pathSeparator;

    /**
     * Simple separator
     * @see File
     */
    static final String SEPARATOR = File.separator;

    /**
     * Double {@code INDENT}
     */
    static final String DOUBLE_INDENT = INDENT + INDENT;

    /**
     * Double {@code LINE_SEPARATOR}
     */
    static final String DOUBLE_LINE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;

    /**
     * Just write the error message.
     *
     * @param errorMessage information about error
     */
    static void writeErrorMessage(String errorMessage) {
        System.err.println(errorMessage);
    }
}
