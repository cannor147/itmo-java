package ru.ifmo.rain.bashunov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ru.ifmo.rain.bashunov.hello.BasicService.*;

public class HelloUDPClient implements HelloClient {

    private static final int SO_TIMEOUT = 50;
    private static final int CAPACITY = 1024;

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println(getUsage());
        }

        try {
            String name = args[0];
            int port = Integer.parseInt(args[1]);
            String prefix = args[2];
            int threads = Integer.parseInt(args[3]);
            int requests = Integer.parseInt(args[4]);

            HelloClient client = new HelloUDPClient();
            client.run(name, port, prefix, threads, requests);
        } catch (NumberFormatException e) {
            System.out.println(getUsage());
        }
    }

    @Override
    public void run(String name, int port, String prefix, int threads, int requests) {
        validate(threads);

        InetAddress ip;
        try {
            ip = InetAddress.getByName(name);
        } catch (IOException e) {
            writeErrorMessage("Cannot get the ip address for " + name);
            return;
        }

        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            int threadIndex = i;
            Thread thread = new Thread(() -> {
                try(DatagramSocket socket = new DatagramSocket()) {
                    socket.setSoTimeout(SO_TIMEOUT);

                    for (int j = 0; j < requests; j++) {
                        byte[] message = getMessage(prefix, threadIndex, j);
                        DatagramPacket sendingPacket = new DatagramPacket(message, message.length, ip, port);

                        String result = "";
                        String expected = "Hello, " + prefix + threadIndex + "_" + j;

                        while (!result.equals(expected)) {
                            try {
                                socket.send(sendingPacket);

                                byte[] receivingData = new byte[CAPACITY];
                                DatagramPacket receivingPacket = new DatagramPacket(receivingData, CAPACITY);
                                socket.receive(receivingPacket);

                                result = new String(receivingPacket.getData(), receivingPacket.getOffset(), receivingPacket.getLength());
                            } catch (SocketTimeoutException e) {
                                writeErrorMessage("Timed out");
                            } catch (SocketException e) {
                                writeErrorMessage("Cannot open socket");
                            } catch (SecurityException e) {
                                writeErrorMessage("Security problem");
                                e.printStackTrace();
                            } catch (IOException e) {
                                writeErrorMessage("Problem with sending data");
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (SocketException e) {
                    writeErrorMessage("Cannot open socket");
                } catch (SecurityException e) {
                    writeErrorMessage("Security problem");
                    e.printStackTrace();
                }
            });

            thread.start();
            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
                // ignored
            }
        }
    }

    private static byte[] getMessage(String prefix, int threadNumber, int requestNumber) {
        String result = prefix + threadNumber + "_" + requestNumber;
        return result.getBytes(StandardCharsets.UTF_8);
    }

    private static String getUsage() {
        return "HelloUDClient [name] [port] [prefix] [threads] [requests]";
    }
}
