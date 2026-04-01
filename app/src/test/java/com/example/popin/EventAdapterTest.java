package com.example.popin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

import com.example.popin.logic.EventAdapter;
import com.example.popin.logic.EventItem;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class EventAdapterTest {

    private EventAdapter createAdapter() {
        List<EventItem> events = Arrays.asList(
                new EventItem("SOEN Mixer", "March 20, 2026 - 6:00 PM", "EV Building Lobby", "Networking event for SOEN students."),
                new EventItem("Board Games Night", "March 22, 2026 - 7:30 PM", "Hall A", "Fun evening with board games and snacks."),
                new EventItem("Hackathon Kickoff", "March 24, 2026 - 5:00 PM", "Room H-937", "Kickoff meeting for the hackathon."),
                new EventItem("Coffee and Code", "March 26, 2026 - 2:00 PM", "Library Cafe", "Coding session with coffee and classmates."),
                new EventItem("AI Study Jam", "March 28, 2026 - 4:30 PM", "Engineering Lounge", "Group study session for AI topics.")
        );
        return new EventAdapter(events);
    }

    @Test
    public void initialState_showsAllEvents() {
        EventAdapter adapter = createAdapter();
        assertEquals(5, adapter.getItemCount());
    }

    @Test
    public void filter_byTitle_isCaseInsensitive() {
        EventAdapter adapter = createAdapter();
        adapter.filter("hackathon");
        assertEquals(1, adapter.getItemCount());

        adapter.filter("HaCkAtHoN");
        assertEquals(1, adapter.getItemCount());
    }

    @Test
    public void filter_byLocationAndDate_supportsTrimmedQuery() {
        EventAdapter adapter = createAdapter();
        adapter.filter("  hall a  ");
        assertEquals(1, adapter.getItemCount());

        adapter.filter("march 26");
        assertEquals(1, adapter.getItemCount());
    }

    @Test
    public void filter_emptyOrNull_restoresAllEvents() {
        EventAdapter adapter = createAdapter();
        adapter.filter("ai");
        assertEquals(1, adapter.getItemCount());

        adapter.filter("");
        assertEquals(5, adapter.getItemCount());

        adapter.filter(null);
        assertEquals(5, adapter.getItemCount());
    }

    @Test
    public void filter_noMatch_showsNoEvents() {
        EventAdapter adapter = createAdapter();
        adapter.filter("this does not exist");
        assertEquals(0, adapter.getItemCount());
    }
}