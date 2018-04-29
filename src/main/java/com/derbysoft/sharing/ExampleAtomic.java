package com.derbysoft.sharing;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class ExampleAtomic {

    public static void main(String[] args) {
        AtomicBoolean isRunning = new AtomicBoolean(false);
        final int[] threadCount = {0};
        ExecutorService executor = Executors.newFixedThreadPool(1000);
        CompletionService<Void> completionService = new ExecutorCompletionService(executor);
        IntStream.range(0, 10000).forEach(v -> {
            completionService.submit(() -> {
                if (isRunning.get()) {
                    return null;
                }
                isRunning.set(true);
                threadCount[0]++;
                System.out.println(threadCount[0]);
                if (threadCount[0] != 1) {
                    throw new IllegalStateException("Two thread entered at same time");
                }
                threadCount[0]--;
                isRunning.set(false);
                return null;
            });
        });

        IntStream.range(0, 10000).forEach(v -> {
            try {
                completionService.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }
}
