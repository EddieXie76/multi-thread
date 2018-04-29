package com.derbysoft.sharing;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ExampleSynchronize {
    private void makeReservation(Reservation reservation) {
        synchronized (reservation.getReservationNumber()) {
            //do something
        }
    }

    private void cancelReservation(Reservation reservation) {
        synchronized (reservation.getReservationNumber()) {
            //do something
        }
    }

    public static void main(String[] args) {
        ExampleVolatile exampleVolatile = new ExampleVolatile();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        CompletionService<Void> completionService = new ExecutorCompletionService(executor);
        int loop = 100;
        IntStream.range(0, loop).forEach(v -> {
            completionService.submit(() -> {
                new ExampleSynchronize().log(v,"A", "B");
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
    }

    public synchronized void log(int v, String msg1, String msg2) throws InterruptedException {
        System.out.println("V=" + v + " MSG1=" + msg1);
        Thread.sleep(v % 50);
        System.out.println("V=" + v + " MSG2=" +  msg2);
    }
}
