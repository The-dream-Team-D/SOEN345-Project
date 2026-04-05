package com.example.popin.addedfiles;

import java.util.HashMap;
import com.example.popin.logic.User;

public class Customer extends User {
    private String phoneNumber;
    private HashMap<Integer, Reservation> reservations;


    public Customer(String email, String password, String phoneNumber) {
        super(email, password);

        this.phoneNumber = phoneNumber;
        this.reservations = new HashMap<>();
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public HashMap<Integer, Reservation> getReservations() {
        return reservations;
    }

    public void addReservation(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }

    public void cancelReservation(int reservationId) {
        reservations.remove(reservationId);
    }
}

