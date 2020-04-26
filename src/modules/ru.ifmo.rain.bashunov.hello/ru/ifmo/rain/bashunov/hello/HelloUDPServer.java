package ru.ifmo.rain.bashunov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ru.ifmo.rain.bashunov.hello.BasicService.*;

public class HelloUDPServer implements HelloServer {

    private static final int SO_TIMEOUT = 100;
    private static final int CAPACITY = 1024;

    private boolean started;
    private DatagramSocket socket;
    private List<Thread> threadList;

    @SuppressWarnings("WeakerAccess")
    public HelloUDPServer() {
        this.started = false;
        threadList = new ArrayList<>();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(getUsage());
        }

        try {
            int port = Integer.parseInt(args[0]);
            int threads = Integer.parseInt(args[1]);

            HelloServer server = new HelloUDPServer();
            server.start(port, threads);
        } catch (NumberFormatException e) {
            System.out.println(getUsage());
        }
    }

    @Override
    public void start(int port, int threads) {
        validate(threads);
        if (started) {
            throw new IllegalStateException("Server have been already started");
        }

        try {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(SO_TIMEOUT);

            for (int i = 0; i < threads; i++) {
                Thread thread = new Thread(() -> {
                    try {
                        while (!socket.isClosed()) {
                            byte[] receivingData = new byte[CAPACITY];
                            DatagramPacket receivingPacket = new DatagramPacket(receivingData, CAPACITY);
                            socket.receive(receivingPacket);

                            InetAddress clientIP = receivingPacket.getAddress();
                            int clientPort = receivingPacket.getPort();

                            byte[] sendingData = getMessage("Hello, " + new String(receivingPacket.getData(), receivingPacket.getOffset(), receivingPacket.getLength()));
                            DatagramPacket sendingPacket = new DatagramPacket(sendingData, sendingData.length, clientIP, clientPort);
                            socket.send(sendingPacket);
                        }
                    } catch (SocketTimeoutException ignored) {
                        writeErrorMessage("Timed out");
                    } catch (SocketException ignored) {
                        writeErrorMessage("Cannot open socket");
                    } catch (SecurityException e) {
                        writeErrorMessage("Security problem");
                        e.printStackTrace();
                    } catch (IOException e) {
                        writeErrorMessage("Problem with sending data");
                        e.printStackTrace();
                    }
                });

                thread.start();
                threadList.add(thread);
            }
        } catch (SocketException e) {
            writeErrorMessage("Cannot open socket");
        }
    }

    @Override
    public void close() {
        started = false;
        socket.close();

        for (Thread thread : threadList) {
            thread.interrupt();
        }
        threadList = new ArrayList<>();
    }

    private static byte[] getMessage(String message) {
        return message.getBytes(StandardCharsets.UTF_8);
    }

    private static String getUsage() {
        return "HelloUDPServer [port] [threads]";
    }
}
