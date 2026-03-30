package com.example.popin;

import java.util.HashMap;
import java.util.Map;

public class Customer extends User {
    private String phoneNumber;
    private final Map<Integer, Reservation> reservations;

    public Customer(String name, String email, String password, String phoneNumber) {
        super(email, password);
        setName(name);
        setIsAdmin(false);
        this.phoneNumber = phoneNumber;
        this.reservations = new HashMap<>();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Map<Integer, Reservation> getReservations() {
        return reservations;
    }

    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            reservations.put(reservation.getId(), reservation);
        }
    }

    public void cancelReservation(int reservationId) {
        reservations.remove(reservationId);
    }
}
