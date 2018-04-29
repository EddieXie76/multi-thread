package com.derbysoft.sharing;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ExampleExecutor {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 10, 10,
                TimeUnit.MINUTES, new LinkedBlockingDeque<>(), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("Discard");
            }
        });
        IntStream.range(1000, 20000000).forEach(value -> {
            executor.submit(() -> {
                try {
                    System.out.println(String.format("%d, %d, %d, %d",
                            executor.getActiveCount(),
                            executor.getCorePoolSize(),
                            executor.getCompletedTaskCount(),
                            executor.getQueue().size()));
                    Thread.sleep(value%100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        Thread.sleep(100000);
    }
}
