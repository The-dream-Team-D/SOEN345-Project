package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class MyTicketsActivityFlowTest {

    private MockedStatic<FirebaseDatabase> mockedFirebase;

    @Before
    public void setUp() {
        User user = new User("student@email.com", "pass");
        user.setIsAdmin(false);
        UserInSession.create(user);
    }

    @After
    public void tearDown() {
        if (mockedFirebase != null) {
            mockedFirebase.close();
        }
        UserInSession.clear();
    }

    @Test
    public void onCreate_withoutSession_finishes() {
        UserInSession.clear();
        MyTicketsActivity activity = Robolectric.buildActivity(MyTicketsActivity.class)
                .setup()
                .get();
        assertTrue(activity.isFinishing());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void onCreate_fetchAndEnrichTickets_populatesList() throws Exception {
        FirebaseDatabase mockDb = mock(FirebaseDatabase.class);
        DatabaseReference userTicketsRoot = mock(DatabaseReference.class);
        DatabaseReference userTicketsRef = mock(DatabaseReference.class);
        DatabaseReference eventsRef = mock(DatabaseReference.class);

        when(mockDb.getReference("User tickets")).thenReturn(userTicketsRoot);
        when(mockDb.getReference("Event database")).thenReturn(eventsRef);
        when(userTicketsRoot.child(anyString())).thenReturn(userTicketsRef);

        DataSnapshot titleNode = mock(DataSnapshot.class);
        when(titleNode.getValue(String.class)).thenReturn("SOEN Mixer");
        DataSnapshot ticketNode = mock(DataSnapshot.class);
        when(ticketNode.child("title")).thenReturn(titleNode);
        when(ticketNode.getKey()).thenReturn("ticket-1");
        DataSnapshot ticketRoot = mock(DataSnapshot.class);
        when(ticketRoot.getChildren()).thenReturn(Collections.singletonList(ticketNode));
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(ticketRoot);
            return null;
        }).when(userTicketsRef).addValueEventListener(any());

        EventItem event = new EventItem("SOEN Mixer", "March 20, 2026 - 6:00 PM", "EV Building Lobby", "Details");
        DataSnapshot eventNode = mock(DataSnapshot.class);
        when(eventNode.getValue(EventItem.class)).thenReturn(event);
        DataSnapshot eventsRoot = mock(DataSnapshot.class);
        when(eventsRoot.getChildren()).thenReturn(Collections.singletonList(eventNode));
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(eventsRoot);
            return null;
        }).when(eventsRef).addListenerForSingleValueEvent(any());

        mockedFirebase = mockStatic(FirebaseDatabase.class);
        mockedFirebase.when(FirebaseDatabase::getInstance).thenReturn(mockDb);

        MyTicketsActivity activity = Robolectric.buildActivity(MyTicketsActivity.class)
                .setup()
                .get();

        RecyclerView rv = activity.findViewById(R.id.rvTickets);
        assertNotNull(rv.getAdapter());
        assertEquals(1, rv.getAdapter().getItemCount());

        Field ticketListField = MyTicketsActivity.class.getDeclaredField("ticketList");
        ticketListField.setAccessible(true);
        List<TicketItem> tickets = (List<TicketItem>) ticketListField.get(activity);
        assertEquals("March 20, 2026 - 6:00 PM", tickets.get(0).getDateTime());
        assertEquals("EV Building Lobby", tickets.get(0).getLocation());
    }
}
