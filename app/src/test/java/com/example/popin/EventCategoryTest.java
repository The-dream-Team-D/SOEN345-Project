package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.popin.logic.EventCategory;

import org.junit.Test;

import java.util.Arrays;

public class EventCategoryTest {

    @Test
    public void enum_containsExpectedValues() {
        assertEquals(5, EventCategory.values().length);
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.Social));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.Educational));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.Professional));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.Sports));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.Entertainment));
    }

    @Test
    public void valueOf_returnsMatchingEnum() {
        assertEquals(EventCategory.Social, EventCategory.valueOf("Social"));
        assertEquals(EventCategory.Entertainment, EventCategory.valueOf("Entertainment"));
    }
}
