package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.popin.addedfiles.Customer;
import com.example.popin.addedfiles.Event;
import com.example.popin.addedfiles.Reservation;
import com.example.popin.logic.EventCategory;

import org.junit.Test;

import java.util.Date;

public class ReservationTest {

    @Test
    public void constructor_setsCustomerEventAndGeneratedId() {
        Customer customer = new Customer("customer@example.com", "secret", "514");
        Event event = new Event("SOEN Mixer", "EV", "Details", new Date(System.currentTimeMillis() + 60_000), EventCategory.SOCIAL);

        Reservation reservation = new Reservation(customer, event);

        assertEquals(customer, reservation.getCustomer());
        assertEquals(event, reservation.getEvent());
    }

    @Test
    public void reservations_receiveDifferentIds() {
        Customer customer = new Customer("customer@example.com", "secret", "514");
        Event firstEvent = new Event("A", "L1", "D1", new Date(System.currentTimeMillis() + 60_000), EventCategory.SOCIAL);
        Event secondEvent = new Event("B", "L2", "D2", new Date(System.currentTimeMillis() + 120_000), EventCategory.SPORTS);

        Reservation first = new Reservation(customer, firstEvent);
        Reservation second = new Reservation(customer, secondEvent);

        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    public void setters_updateCustomerAndEvent() {
        Customer customer1 = new Customer("one@example.com", "secret", "111");
        Customer customer2 = new Customer("two@example.com", "secret", "222");
        Event event1 = new Event("A", "L1", "D1", new Date(System.currentTimeMillis() + 60_000), EventCategory.SOCIAL);
        Event event2 = new Event("B", "L2", "D2", new Date(System.currentTimeMillis() + 120_000), EventCategory.ENTERTAINMENT);

        Reservation reservation = new Reservation(customer1, event1);
        reservation.setCustomer(customer2);
        reservation.setEvent(event2);

        assertEquals(customer2, reservation.getCustomer());
        assertEquals(event2, reservation.getEvent());
    }
}

