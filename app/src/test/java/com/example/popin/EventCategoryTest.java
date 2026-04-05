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
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.SOCIAL));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.EDUCATIONAL));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.PROFESSIONAL));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.SPORTS));
        assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.ENTERTAINMENT));
    }

    @Test
    public void valueOf_returnsMatchingEnum() {
        assertEquals(EventCategory.SOCIAL, EventCategory.valueOf("SOCIAL"));
        assertEquals(EventCategory.ENTERTAINMENT, EventCategory.valueOf("ENTERTAINMENT"));
    }
}

