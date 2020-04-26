package ru.ifmo.rain.bashunov.runner;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import ru.ifmo.rain.bashunov.runner.service.components.Module;
import static ru.ifmo.rain.bashunov.runner.service.BasicService.*;


public class PathService {

    @SuppressWarnings("WeakerAccess")
    public static final String ORACLE_PATH = "https://docs.oracle.com/en/java/javase/11/docs/api/";

    public static final String TEMP_PATH = "./tmp";

    public static final String ARTIFACTS_PATH = "./test/artifacts";
    public static final String LIB_PATH = "./test/lib";
    public static final String MODULES_PATH = "./test/modules";

    public static final String OUT_PATH = "./out/production";

    public static String getJavadocPath() {
        return JAVADOC_PATH;
    }

    public static final String JAVADOC_PATH = "./out/javadoc";
    public static final String SRC_PATH = "./src/modules";

    static String getClassPath(List<Module> modules) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < modules.size(); i++) {
            stringBuilder.append(modules.get(i).getProductPath());
            if (i != modules.size() - 1) stringBuilder.append(PATH_SEPARATOR);
        }
        return ARTIFACTS_PATH + PATH_SEPARATOR +
                LIB_PATH + PATH_SEPARATOR +
                MODULES_PATH + PATH_SEPARATOR +
                stringBuilder.toString();
    }

    static String getOraclePath() {
        return ORACLE_PATH;
    }

    static String javadocHtml(String yourClass) {
        String file = JAVADOC_PATH + SEPARATOR + yourClass.replace(".java", ".html");
        Path path;
        try {
            path = Paths.get(file).toAbsolutePath().normalize();
        } catch (InvalidPathException e) {
            return "You haven't access to link";
        }
        return path.toString();
    }
}
