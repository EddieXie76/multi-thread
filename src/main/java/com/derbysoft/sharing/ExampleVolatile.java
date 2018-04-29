package com.derbysoft.sharing;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ExampleVolatile {
    volatile int value = 0;

    public void inc() {
        value++;
    }

    public static void main(String[] args) {
        ExampleVolatile exampleVolatile = new ExampleVolatile();
        ExecutorService executor = Executors.newFixedThreadPool(1000);
        CompletionService<Void> completionService = new ExecutorCompletionService(executor);
        int loop = 10000;
        IntStream.range(0, loop).forEach(v -> {
            completionService.submit(() -> {
                exampleVolatile.inc();
                Thread.sleep(v % 100);
                return null;
            });
        });

        IntStream.range(0, loop).forEach(v -> {
            try {
                completionService.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
        System.out.println(exampleVolatile.value);
    }
}
