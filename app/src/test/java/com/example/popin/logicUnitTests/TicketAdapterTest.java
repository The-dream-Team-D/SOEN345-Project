package com.example.popin.logicUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.test.core.app.ApplicationProvider;

import com.example.popin.R;
import com.example.popin.logic.TicketAdapter;
import com.example.popin.logic.TicketItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class TicketAdapterTest {

    @Before
    public void setUp() {
        ApplicationProvider.getApplicationContext()
                .setTheme(com.google.android.material.R.style.Theme_MaterialComponents_Light_NoActionBar);
    }
    private List<TicketItem> sampleTickets() {
        return Arrays.asList(
                new TicketItem("t1", "SOEN Mixer", ticketDate(2026, 2, 20, 18, 0), "EV Building Lobby"),
                new TicketItem("t2", "Hackathon Kickoff", ticketDate(2026, 2, 24, 17, 0), "Room H-937"),
                new TicketItem("t3", "AI Study Jam", ticketDate(2026, 2, 28, 16, 30), "Engineering Lounge")
        );
    }

    private long ticketDate(int year, int month, int day, int hour, int minute) {
        return com.example.popin.logic.EventItem.convertTimeToLong(year, month, day, hour, minute);
    }

    @Test
    public void initialState_showsAllTickets() {
        TicketAdapter adapter = new TicketAdapter(
                sampleTickets(),
                ticket -> { },
                ticket -> { }
        );

        assertEquals(3, adapter.getItemCount());
    }

    @Test
    public void filter_nullOrEmpty_restoresAllTickets() {
        TicketAdapter adapter = new TicketAdapter(
                sampleTickets(),
                ticket -> { },
                ticket -> { }
        );

        adapter.filter("SOEN");
        assertEquals(1, adapter.getItemCount());

        adapter.filter("");
        assertEquals(3, adapter.getItemCount());

        adapter.filter(null);
        assertEquals(3, adapter.getItemCount());
    }

    @Test
    public void cancelButtonClick_callsCancelListener_forBoundTicket() {
        AtomicReference<TicketItem> cancelled = new AtomicReference<>();

        TicketAdapter adapter = new TicketAdapter(
                sampleTickets(),
                cancelled::set,
                ticket -> { }
        );

        ViewGroup parent = new FrameLayout(ApplicationProvider.getApplicationContext());
        TicketAdapter.TicketViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 1);

        holder.itemView.findViewById(R.id.btnCancelTicket).performClick();

        assertNotNull(cancelled.get());
        assertEquals("t2", cancelled.get().getTicketId());
        assertEquals("Hackathon Kickoff", cancelled.get().getTitle());
    }

    @Test
    public void rowClick_callsTicketClickListener_forBoundTicket() {
        AtomicReference<TicketItem> opened = new AtomicReference<>();

        TicketAdapter adapter = new TicketAdapter(
                sampleTickets(),
                ticket -> { },
                opened::set
        );

        ViewGroup parent = new FrameLayout(ApplicationProvider.getApplicationContext());
        TicketAdapter.TicketViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 2);

        holder.itemView.performClick();

        assertNotNull(opened.get());
        assertEquals("t3", opened.get().getTicketId());
        assertEquals("AI Study Jam", opened.get().getTitle());
    }
}
