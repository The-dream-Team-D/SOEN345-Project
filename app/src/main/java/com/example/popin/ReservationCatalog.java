package com.example.popin;
import java.util.HashMap;

public class ReservationCatalog{
    private HashMap<Integer, Reservation> reservations;

    public ReservationCatalog(){
        this.reservations = new HashMap<>();
    }

    public void addReservation(Event event, Customer customer){

        //few checks before adding the reservation
        if(!event.isAvailable()){
            System.out.println("Event is not available for reservation.");
            return;
        }else if (customer == null){
            System.out.println("Customer information is required for reservation.");
            return;
        } else if (event.getDate().before(new java.util.Date())){
            System.out.println("Cannot reserve an event that has already occurred.");
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