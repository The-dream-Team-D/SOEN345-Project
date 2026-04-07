package com.example.popin.logicUnitTests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.popin.logic.EventCategory;
import com.example.popin.logic.EventItem;

public class EventItemTest {

    @Test
    public void constructor_setsAllFields() {
        EventItem item = new EventItem(
                "SOEN Mixer",
                EventItem.convertTimeToLong(2026, 2, 20, 18, 0),
                "EV Building Lobby",
                "Meet other SOEN students, network, and enjoy snacks in a casual social setting.",
                "https://images.stockcake.com/public/9/6/d/96d4100c-ca71-4e09-b84e-d7e90c294a87_large/joyful-party-celebration-stockcake.jpg",
                100,
                EventCategory.SOCIAL
        );

        assertEquals("SOEN Mixer", item.getTitle());
        assertEquals(EventItem.convertTimeToLong(2026, 2, 20, 18, 0), item.getDateTime());
        assertEquals("EV Building Lobby", item.getLocation());
        assertEquals("Meet other SOEN students, network, and enjoy snacks in a casual social setting.", item.getDetails());
    }

    @Test
    public void emptyConstructor_and_setters() {
        EventItem item = new EventItem();

        assertNull(item.getTitle());

        item.setTitle("New Title");
        item.setDateTime(2024, 0, 1, 12, 0);
        item.setLocation("Montreal");
        item.setDetails("Sample event description");

        item.setCapacity(20);
        item.setAttendeeCount(15);

        String imgUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTc9APxkj0xClmrU3PpMZglHQkx446nQPG6lA&s";
        item.setImgURL(imgUrl);
        item.setCategory(EventCategory.ENTERTAINMENT);



        assertEquals("New Title", item.getTitle());
        assertEquals(EventItem.convertTimeToLong(2024, 0, 1, 12, 0), item.getDateTime());
        assertEquals("Montreal", item.getLocation());
        assertEquals("Sample event description", item.getDetails());
        assertEquals(20, item.getCapacity());
        assertEquals(15, item.getAttendeeCount());
        assertEquals(imgUrl, item.getImgURL());
        assertEquals(EventCategory.ENTERTAINMENT, item.getCategory());

        long time = 1775515412247L;
        item.setDateTime(time);

        assertEquals(time, item.getDateTime());
    }

    @Test
    public void setDateTimeFail() {
        EventItem item = new EventItem();

        assertNull(item.getTitle());

        boolean result = item.setDateTime(1969, 0, 1, 12, 0);


        assertEquals(0, item.getDateTime());
        assertFalse(result);
    }


    @Test
    public void convertTimeToLongFailOptions() {

        // CHECK FOR MONTH VALIDITY FAILS
        long result = EventItem.convertTimeToLong(2000, -1, 12, 12, 12);
        assertEquals(-1L, result);

        // CHECK FOR DAY VALIDITY FAILS
        result = EventItem.convertTimeToLong(2000, 11, 0, 12, 12);
        assertEquals(-1L, result);

        // CHECK FOR HOUR VALIDITY FAILS
        result = EventItem.convertTimeToLong(2000, 11, 11, -1, 12);
        assertEquals(-1L, result);

        // CHECK FOR MINUTES VALIDITY FAILS
        result = EventItem.convertTimeToLong(2000, 11, 11, 1, -1);
        assertEquals(-1L, result);

        // FEB 30 PASSES ALL CHECKS BUT THE DATE IS IMPOSSIBLE SO REACHES CATCH
        result = EventItem.convertTimeToLong(2024, 1, 30, 0, 0);
        assertEquals(-1, result);
    }


}

