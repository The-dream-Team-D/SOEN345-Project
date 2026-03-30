package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.test.core.app.ApplicationProvider;

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

    private List<TicketItem> sampleTickets() {
        return Arrays.asList(
                new TicketItem("t1", "SOEN Mixer", "March 20, 2026 - 6:00 PM", "EV Building Lobby"),
                new TicketItem("t2", "Hackathon Kickoff", "March 24, 2026 - 5:00 PM", "Room H-937"),
                new TicketItem("t3", "AI Study Jam", "March 28, 2026 - 4:30 PM", "Engineering Lounge")
        );
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
    public void filter_isCaseInsensitive_andMatchesFields() {
        TicketAdapter adapter = new TicketAdapter(
                sampleTickets(),
                ticket -> { },
                ticket -> { }
        );

        adapter.filter("hackathon");
        assertEquals(1, adapter.getItemCount());

        adapter.filter("ENGINEERING LOUNGE");
        assertEquals(1, adapter.getItemCount());

        adapter.filter("March 20");
        assertEquals(1, adapter.getItemCount());
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
