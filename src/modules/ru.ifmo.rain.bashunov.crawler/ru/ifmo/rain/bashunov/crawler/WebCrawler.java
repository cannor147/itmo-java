package ru.ifmo.rain.bashunov.crawler;

import info.kgeorgiy.java.advanced.crawler.Crawler;
import info.kgeorgiy.java.advanced.crawler.Document;
import info.kgeorgiy.java.advanced.crawler.Downloader;
import info.kgeorgiy.java.advanced.crawler.Result;
import ru.ifmo.rain.bashunov.mapper.utils.SmartCounter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static info.kgeorgiy.java.advanced.crawler.URLUtils.getHost;

public class WebCrawler implements Crawler {

    private Downloader downloader;
    private final ExecutorService downloadService, extractService;
    private final Map<String, SmartCounter> hosts;
    private int perHost;

    @SuppressWarnings("WeakerAccess")
    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        validate(downloaders, extractors, perHost);
        this.downloader = downloader;
        this.downloadService = new ThreadPoolExecutor(downloaders, downloaders, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));
        this.extractService = new ThreadPoolExecutor(extractors, extractors, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));
        this.hosts = new ConcurrentHashMap<>();
        this.perHost = perHost;
    }

    @Override
    public Result download(String url, int depth) {
        validateDepth(depth);
        Set<String> checkedLinks = new HashSet<>();
        Map<String, IOException> errors = new HashMap<>();
        BlockingQueue<Map.Entry<String, Integer>> queue = new ArrayBlockingQueue<>(10000);

        if (depth > 0) {
            queue.add(Map.entry(url, depth));
            checkedLinks.add(url);
        }

        SmartCounter counter = new SmartCounter((depth > 0) ? 1 : 0);
        while (!queue.isEmpty()) {
            Map.Entry<String, Integer> page = queue.poll();

            downloadService.execute(() -> downloadPage(queue, page, errors, checkedLinks, counter));

            try {
                int val = counter.getValue();
                while (val != 0) {

                }
                if (val == 0) continue;
                counter.waitUntilValue(val);
            } catch (InterruptedException e) {
                writeErrorMessage("Thread interrupted");
            }
        }

        try {
            counter.waitForValue(0);
        } catch (InterruptedException ignored) {
            writeErrorMessage("Thread interrupted");
        }

        List<String> downloadedLinks = checkedLinks.stream().filter((s) -> !errors.containsKey(s)).collect(Collectors.toList());
        return new Result(downloadedLinks, errors);
    }

    @Override
    public void close() {
        downloadService.shutdownNow();
        extractService.shutdownNow();
    }

    private void downloadPage(Queue<Map.Entry<String, Integer>> queue, Map.Entry<String, Integer> page, Map<String, IOException> errors,
                              Set<String> checkedLinks, SmartCounter counter) {
        String pageUrl = page.getKey();
        Integer pageDepth = page.getValue();

        try {
            String currentHost = getHost(pageUrl);
            synchronized (hosts) {
                if (!hosts.containsKey(currentHost)) {
                    hosts.put(currentHost, new SmartCounter());
                }
            }

            SmartCounter hostCounter = hosts.get(currentHost);
            hostCounter.incrementWithUpperBound(perHost);

            try {
                Document document = downloader.download(pageUrl);

                if (pageDepth > 1) {
                    counter.increment();
                    extractService.execute(() -> this.extractLinks(queue, page, errors, checkedLinks, counter, document));
                }
            } catch (IOException e) {
                errors.put(pageUrl, e);
            } finally {
                hostCounter.decrement();
            }
        } catch (MalformedURLException e) {
            writeErrorMessage(String.format("Link %s is incorrect", pageUrl));
        } catch (InterruptedException e) {
            writeErrorMessage("Thread interrupted");
        } finally {
            counter.decrement();
        }
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private void extractLinks(Queue<Map.Entry<String, Integer>> queue, Map.Entry<String, Integer> page, Map<String, IOException> errors,
                              Set<String> checkedLinks, SmartCounter counter, Document document) {
        String pageUrl = page.getKey();
        Integer pageDepth = page.getValue();

        try {
            List<String> links = document.extractLinks();
            for (String link : links) {
                synchronized (checkedLinks) {
                    if (!checkedLinks.contains(link)) {
                        queue.add(Map.entry(link, pageDepth - 1));
                        counter.increment();
                        checkedLinks.add(link);
                    }
                    checkedLinks.notify();
                }
            }
        } catch (IOException e) {
            errors.put(pageUrl, e);
        } finally {
            synchronized (queue) {
                queue.notify();
            }
            counter.decrement();
        }
    }

    private void validateDepth(int depth) throws IllegalArgumentException {
        if (depth < 0) throw new IllegalArgumentException("Depth cannot be less than zero");
    }

    private void validate(int downloaders, int extractor, int perHost) throws IllegalArgumentException {
        if (downloaders <= 0) throw new IllegalArgumentException("Number of downloaders cannot be less or equals zero");
        if (extractor <= 0) throw new IllegalArgumentException("Number of extractor cannot be less or equals zero");
        if (perHost <= 0) throw new IllegalArgumentException("Number of links per host cannot be less or equals zero");
    }

    /**
     * Just write the error message.
     *
     * @param errorMessage information about error
     */
    private static void writeErrorMessage(String errorMessage) {
        System.err.println(errorMessage);
    }
}
