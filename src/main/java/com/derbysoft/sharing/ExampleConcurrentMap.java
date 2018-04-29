package com.derbysoft.sharing;

import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ExampleConcurrentMap {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> tagCountMap = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(1000);
        CompletionService<Void> completionService = new ExecutorCompletionService(executor);
        String[] tags = "a b c d".split(" ");
        int loop = 10000;
        for (String tag : tags) {
            IntStream.range(0, loop).forEach(v -> {
                System.out.println(tag + v);
                completionService.submit(() -> {
                    if (!tagCountMap.containsKey(tag)) {
                        tagCountMap.put(tag, new Integer(1));
                        return null;
                    }
                    tagCountMap.put(tag, tagCountMap.get(tag) + 1);
                    return null;
                });
            });
        }

        for (String tag : tags) {
            IntStream.range(0, loop).forEach(v -> {
                try {
                    completionService.take().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();

        for (Map.Entry<String, Integer> entry : tagCountMap.entrySet()) {
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }
    }
}
