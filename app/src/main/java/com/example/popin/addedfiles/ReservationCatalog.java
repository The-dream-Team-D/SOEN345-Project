package com.example.popin.addedfiles;

import java.util.HashMap;

public class ReservationCatalog{
    private HashMap<Integer, Reservation> reservations;

    public ReservationCatalog(){
        this.reservations = new HashMap<>();
    }

    public void addReservation(Event event, Customer customer){

        //few checks before adding the reservation
        if(!event.isAvailable()){
            return;
        }else if (customer == null){
            return;
        } else if (event.getDate().before(new java.util.Date())){
            return;
        }

        Reservation reservation = new Reservation(customer, event);
        reservations.put(reservation.getId(), reservation);
    }

    public void cancelReservation(int id){
        reservations.remove(id);
    }

    public Reservation getReservation(int id){
        return reservations.get(id);
    }
}
