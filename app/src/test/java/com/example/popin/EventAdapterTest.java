package com.example.popin;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class EventAdapterTest {

    private List<EventItem> getSampleEvents() {
        return Arrays.asList(
                new EventItem("SOEN Mixer", "March 20", "EV Building"),
                new EventItem("Board Games", "March 22", "Hall A")
        );
    }

    private EventAdapter createAdapter() {
        return new EventAdapter(getSampleEvents());
    }

    @Test
    public void onBindViewHolder_setsCorrectText() {
        EventAdapter adapter = createAdapter();
        LinearLayout parent = new LinearLayout(RuntimeEnvironment.getApplication());
        
        // Create the ViewHolder
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        
        // Bind the first item (SOEN Mixer)
        adapter.onBindViewHolder(holder, 0);
        
        TextView title = holder.itemView.findViewById(R.id.tvEventTitle);
        TextView date = holder.itemView.findViewById(R.id.tvEventDateTime);
        
        assertEquals("SOEN Mixer", title.getText().toString());
        assertEquals("March 20", date.getText().toString());
    }

    @Test
    public void updateList_refreshesData() {
        EventAdapter adapter = createAdapter();
        assertEquals(2, adapter.getItemCount());

        List<EventItem> newList = new ArrayList<>();
        newList.add(new EventItem("New Event", "Now", "Here"));
        adapter.updateList(newList);
        assertEquals(1, adapter.getItemCount());
    }

    @Test
    public void filter_emptyOrNull_restoresAllEvents() {
        EventAdapter adapter = createAdapter();
        adapter.filter("mixer");
        assertEquals(1, adapter.getItemCount());

        adapter.filter(null);
        assertEquals(2, adapter.getItemCount());
    }
}
