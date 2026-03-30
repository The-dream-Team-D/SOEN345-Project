package com.example.popin;

public class Reservation {
    private static int idCounter = 0;
    private int id;
    private Customer customer;
    private Event event;

    public Reservation(Customer customer, Event event){
        this.id = ++idCounter;
        this.customer = customer;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Event getEvent() {
        return event;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


}
