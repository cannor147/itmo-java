package ru.ifmo.rain.bashunov.walk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static ru.ifmo.rain.bashunov.walk.BasicService.*;

@SuppressWarnings("unused")
public class RecursiveWalk {

    private static final int BLOCK_SIZE = 1024 * 1024;
    private static final int HASH_CONST = 0x811c9dc5;

    public static void main(String[] args) {
        RecursiveWalk recursiveWalk = new RecursiveWalk();
        if (args == null || Arrays.stream(args).anyMatch(Objects::isNull) || args.length != 2) {
            writeErrorMessage("Your arguments are incorrect");
            usage();
            return;
        }
        Path inputFilePath, outputFilePath;
        try {
            inputFilePath = Paths.get(args[0]);
        } catch (InvalidPathException e) {
            writeErrorMessage("Your input file name is not valid");
            usage();
            return;
        }
        try {
            outputFilePath = Paths.get(args[1]);
        } catch (InvalidPathException e) {
            writeErrorMessage("Your output file name is not valid");
            usage();
            return;
        }
        try {
            recursiveWalk.walk(inputFilePath, outputFilePath);
        } catch (InvalidPathException e) {
            writeErrorMessage(e.getReason());
        }
    }



    // throw InvalidPathException if paths in arguments are invalid
    public void walk(Path inputFilePath, Path outputFilePath) throws InvalidPathException {
        try {
            Path parent = outputFilePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            throw new InvalidPathException(outputFilePath.toString(), "Cannot create output file with this path");
        }
        try (BufferedWriter outputWriter = Files.newBufferedWriter(outputFilePath)) {
            try (BufferedReader inputReader = Files.newBufferedReader(inputFilePath)) {
                String line;
                while ((line = inputReader.readLine()) != null) {
                    Path filePath;
                    try {
                        filePath = Paths.get(line);
                        Stream<Path> pathStream = Files.walk(filePath);
                        pathStream.filter(Predicate.not(Files::isDirectory)).forEach(getConsumer(outputWriter));
                    } catch (IOException | InvalidPathException e) {
                        write(outputWriter, line, 0);
                        writeErrorMessage("You have mistake in path: " + line);
                    }
                }
            } catch (IOException e) {
                writeErrorMessage("Cannot open input file");
            }
        } catch (IOException e) {
            writeErrorMessage("Cannot open output file");
        }
    }

    private int hash(final byte[] bytes, final int count, int hash) {
        for (int i = 0; i < count; i++) {
            hash = (hash * 0x01000193) ^ (bytes[i] & 0xff);
        }
        return hash;
    }

    private int getHashSum(final Path filePath) {
        byte[] bytes = new byte[BLOCK_SIZE];
        int hashSum = HASH_CONST;
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            int size;
            while ((size = inputStream.read(bytes)) >= 0) {
                hashSum = hash(bytes, size, hashSum);
            }
        } catch (IOException e) {
            writeErrorMessage("Cannot read file " + filePath.toString());
            return 0;
        }
        return hashSum;
    }

    private <T extends Path> Consumer<? super T> getConsumer(BufferedWriter outputWriter) {
        return (path -> write(outputWriter, path.toString(), getHashSum(path)));
    }

    private void write(BufferedWriter outputWriter, final String path, int hashSum) {
        try {
            outputWriter.write(String.format("%08x %s", hashSum, path) + System.lineSeparator());
        } catch (IOException e) {
            writeErrorMessage("Something is wrong with output file");
        }
    }

    /**
     * Write how to run {@code main(String[] args)} correctly
     */
    private static void usage() {
        System.out.println("Usage:");
        System.out.println(TAB + "RecursiveWalk [input file name] [output file name]");
    }
}
