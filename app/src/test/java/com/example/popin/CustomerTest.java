package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Date;

public class CustomerTest {

    @Test
    public void constructor_initializesPhoneAndEmptyReservations() {
        Customer customer = new Customer("customer@example.com", "secret", "514-000-0000");

        assertEquals("514-000-0000", customer.getPhoneNumber());
        assertNotNull(customer.getReservations());
        assertTrue(customer.getReservations().isEmpty());
    }

    @Test
    public void setPhoneNumber_updatesValue() {
        Customer customer = new Customer("customer@example.com", "secret", "111");

        customer.setPhoneNumber("222");

        assertEquals("222", customer.getPhoneNumber());
    }

    @Test
    public void addReservation_storesReservationById() {
        Customer customer = new Customer("customer@example.com", "secret", "514");
        Reservation reservation = new Reservation(
                customer,
                new Event("SOEN Mixer", "EV", "Details", new Date(System.currentTimeMillis() + 60_000), EventCategory.CONCERT)
        );

        customer.addReservation(reservation);

        assertEquals(reservation, customer.getReservations().get(reservation.getId()));
    }

    @Test
    public void cancelReservation_removesReservation() {
        Customer customer = new Customer("customer@example.com", "secret", "514");
        Reservation reservation = new Reservation(
                customer,
                new Event("SOEN Mixer", "EV", "Details", new Date(System.currentTimeMillis() + 60_000), EventCategory.CONCERT)
        );
        customer.addReservation(reservation);

        customer.cancelReservation(reservation.getId());

        assertNull(customer.getReservations().get(reservation.getId()));
    }
}
