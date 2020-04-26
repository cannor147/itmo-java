package ru.ifmo.rain.bashunov.walk;

import java.io.File;

public final class BasicService {

    public static final String ENCODING = "UTF8";

    public static final String AUTHOR_NAME = "Erofey Bashunov";
    public static final String MANIFEST_VERSION = "1.0";

    public static final String TAB = "\t";
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String PATH_SEPARATOR = File.pathSeparator;
    public static final String SEPARATOR = File.separator;

    public static final String D_TAB = TAB + TAB;
    public static final String D_LINE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;

    @SuppressWarnings("WeakerAccess")
    public static final String ORACLE_PATH = "https://docs.oracle.com/en/java/javase/11/docs/api/";
    public static final String TEMP_PATH = "/tmp";

    public static final String ARTIFACTS_PATH = "./tests/artifacts";
    public static final String LIB_PATH = "./tests/lib";
    public static final String MODULES_PATH = "./tests/modules";
    public static final String OUT_PATH = "./out/production/homeworks";
    public static final String SRC_PATH = "./src";

    /**
     * Just write the error message.
     *
     * @param errorMessage information about error
     */
    public static void writeErrorMessage(String errorMessage) {
        System.err.println(errorMessage);
    }
}
