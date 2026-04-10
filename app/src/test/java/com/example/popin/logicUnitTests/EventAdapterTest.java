package com.example.popin.logicUnitTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

import com.example.popin.adapters.EventAdapter;
import com.example.popin.logic.EventCategory;
import com.example.popin.logic.EventItem;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class EventAdapterTest {

    private void applyFilter(EventAdapter adapter, String query) {
        adapter.filter(query, null, false, false);
    }

    private EventAdapter createAdapter() {
        List<EventItem> events = Arrays.asList(
                new EventItem(
                        "SOEN Mixer",
                        EventItem.convertTimeToLong(2026, 2, 20, 18, 0),
                        "EV Building Lobby",
                        "Meet other SOEN students, network, and enjoy snacks in a casual social setting.",
                        "https://images.stockcake.com/public/9/6/d/96d4100c-ca71-4e09-b84e-d7e90c294a87_large/joyful-party-celebration-stockcake.jpg",
                        100,
                        EventCategory.SOCIAL
                ),
                new EventItem(
                        "Board Games Night",
                        EventItem.convertTimeToLong(2026, 2, 22, 19, 30),
                        "Hall A",
                        "Join us for an evening of board games, team challenges, and friendly competition.",
                        "https://cdn.apartmenttherapy.info/image/upload/v1667575155/stock/custom%20stock/2022-11-custom-stock/games-0228-edit.jpg",
                        50,
                        EventCategory.ENTERTAINMENT
                ),
                new EventItem(
                        "Hackathon Kickoff",
                        EventItem.convertTimeToLong(2026, 2, 24, 17, 0),
                        "Room H-937",
                        "Kick off the semester hackathon with team formation, project ideas, and event rules.",
                        "https://ezassi.com/wp-content/uploads/2024/10/hackathon.png",
                        200,
                        EventCategory.PROFESSIONAL
                ),
                new EventItem(
                        "Coffee and Code",
                        EventItem.convertTimeToLong(2026, 2, 26, 14, 0),
                        "Library Cafe",
                        "Bring your laptop, grab a coffee, and code with classmates in a relaxed environment.",
                        "https://localist-images.azureedge.net/photos/52499165824998/card/2d55307e23bf99b05af70bcb92b61f94607cdb85.jpg",
                        40,
                        EventCategory.EDUCATIONAL
                ),
                new EventItem(
                        "AI Study Jam",
                        EventItem.convertTimeToLong(2026, 2, 28, 16, 30),
                        "Engineering Lounge",
                        "Review AI concepts, solve practice problems, and prepare together for upcoming exams.",
                        "https://res.cloudinary.com/startup-grind/image/upload/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-goog/events/On%20Campus%20%283%29_026JzWM.png",
                        60,
                        EventCategory.EDUCATIONAL
                )
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
        applyFilter(adapter, "hackathon");
        assertEquals(1, adapter.getItemCount());

        applyFilter(adapter, "HaCkAtHoN");
        assertEquals(1, adapter.getItemCount());
    }

    @Test
    public void filter_byLocationAndDate_supportsTrimmedQuery() {
        EventAdapter adapter = createAdapter();
        applyFilter(adapter, "  hall a  ");
        assertEquals(1, adapter.getItemCount());

        applyFilter(adapter, "room h-937");
        assertEquals(1, adapter.getItemCount());
    }

    @Test
    public void filter_emptyOrNull_restoresAllEvents() {
        EventAdapter adapter = createAdapter();
        applyFilter(adapter, "ai");
        assertEquals(1, adapter.getItemCount());

        applyFilter(adapter, "");
        assertEquals(5, adapter.getItemCount());

        applyFilter(adapter, null);
        assertEquals(5, adapter.getItemCount());
    }

    @Test
    public void filter_noMatch_showsNoEvents() {
        EventAdapter adapter = createAdapter();
        applyFilter(adapter, "this does not exist");
        assertEquals(0, adapter.getItemCount());
    }
}

