package ru.ifmo.rain.bashunov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Downloader downloader = new CachingDownloader();
            Downloader checkingDownloader = new CheckingDownloader(downloader, 100, 200, 20);
            Crawler crawler = new WebCrawler(checkingDownloader, 100, 200, 20);
            Result result = crawler.download("http://www.filmschoolonline.com/sample_lessons/sample_lessons.htm", 3);
            for (int i = 0; i < result.getDownloaded().size(); i++) {
                System.out.println(result.getDownloaded().get(i));
            }
            System.out.println(result.getErrors().size());
            crawler.close();
        } catch (IOException ignored) {
            // ignored
        }
    }
}
