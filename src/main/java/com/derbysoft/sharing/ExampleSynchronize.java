package com.derbysoft.sharing;

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
}
