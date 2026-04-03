package com.example.popin;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Date;

public class ReservationCatalogTest {

    private MockedStatic<Log> mockedLog;

    @Before
    public void setUp() throws Exception {
        mockedLog = mockStatic(Log.class);
        mockedLog.when(() -> Log.d(anyString(), anyString())).thenReturn(0);

        Field counter = Reservation.class.getDeclaredField("idCounter");
        counter.setAccessible(true);
        counter.set(null, 0);
    }

    @After
    public void tearDown() {
        if (mockedLog != null) {
            mockedLog.close();
        }
    }

    @Test
    public void addReservation_unavailableEvent_doesNotStoreReservation() {
        Event event = org.mockito.Mockito.mock(Event.class);
        org.mockito.Mockito.when(event.isAvailable()).thenReturn(false);

        Customer customer = new Customer("a@test.com", "1234","5145555555");
        ReservationCatalog catalog = new ReservationCatalog();

        catalog.addReservation(event, customer);

        assertNull(catalog.getReservation(1));
    }

    @Test
    public void addReservation_nullCustomer_doesNotStoreReservation() {
        Event event = new Event(
                "Test Event",
                "Hall",
                "Desc",
                new Date(System.currentTimeMillis() + 100000),
                EventCategory.CONCERT
        );

        ReservationCatalog catalog = new ReservationCatalog();

        catalog.addReservation(event, null);

        assertNull(catalog.getReservation(1));
    }

    @Test
    public void addReservation_pastEvent_doesNotStoreReservation() {
        Event event = new Event(
                "Past Event",
                "Hall",
                "Desc",
                new Date(System.currentTimeMillis() - 100000),
                EventCategory.CONCERT
        );

        Customer customer = new Customer("a@test.com", "1234","5145555555");
        ReservationCatalog catalog = new ReservationCatalog();

        catalog.addReservation(event, customer);

        assertNull(catalog.getReservation(1));
    }

    @Test
    public void addReservation_validInput_storesReservation() {
        Event event = new Event(
                "Future Event",
                "Hall",
                "Desc",
                new Date(System.currentTimeMillis() + 100000),
                EventCategory.CONCERT
        );

        Customer customer = new Customer("a@test.com", "1234","5145555555");
        ReservationCatalog catalog = new ReservationCatalog();

        catalog.addReservation(event, customer);

        assertNotNull(catalog.getReservation(1));
    }

    @Test
    public void cancelReservation_existingReservation_removesIt() {
        Event event = new Event(
                "Future Event",
                "Hall",
                "Desc",
                new Date(System.currentTimeMillis() + 100000),
                EventCategory.CONCERT
        );

        Customer customer = new Customer("a@test.com", "1234","5145555555");
        ReservationCatalog catalog = new ReservationCatalog();

        catalog.addReservation(event, customer);
        catalog.cancelReservation(1);

        assertNull(catalog.getReservation(1));
    }
}