package com.derbysoft.sharing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExampleThreadLocal {
    private static ThreadLocal<String> tlIP = ThreadLocal.withInitial(() -> "");

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            send("Hello:127.0.0.1");
            writeLog();
        });
        executorService.submit(() -> {
            send("Good morning;localhost");
            writeLog();
        });

        executorService.submit(() -> {
            send("My name is Eddie:192.168.0.1");
            writeLog();
        });
        Thread.sleep(100);
        executorService.shutdown();
    }

    private static void writeLog() {
        System.out.println(tlIP.get());
    }

    private static void send(String credential) {
        try {
            tlIP.set(credential.split(":")[1]);
        } catch (Exception e) {
        }
    }
}
