package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class EventCategoryTest {

    @Test
    public void enum_containsExpectedValues() {
        assertEquals(6, EventCategory.values().length);
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.CONCERT));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.SPORTS));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.THEATER));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.COMEDY));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.FESTIVAL));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.MUSIC));
    }

    @Test
    public void valueOf_returnsMatchingEnum() {
        assertEquals(EventCategory.CONCERT, EventCategory.valueOf("CONCERT"));
        assertEquals(EventCategory.MUSIC, EventCategory.valueOf("MUSIC"));
    }
}
