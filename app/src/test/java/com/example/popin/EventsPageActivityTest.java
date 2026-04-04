package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
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

import java.util.Collections;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class EventsPageActivityTest {

    private MockedStatic<FirebaseDatabase> mockedFirebase;
    private DatabaseReference eventsRef;

    @Before
    public void setUp() {
        User admin = new User("admin@email.com", "pass");
        admin.setIsAdmin(true);
        UserInSession.create(admin);

        FirebaseDatabase mockDb = mock(FirebaseDatabase.class);
        eventsRef = mock(DatabaseReference.class);
        mockedFirebase = mockStatic(FirebaseDatabase.class);
        mockedFirebase.when(FirebaseDatabase::getInstance).thenReturn(mockDb);
        when(mockDb.getReference("Event database")).thenReturn(eventsRef);
    }

    @After
    public void tearDown() {
        if (mockedFirebase != null) {
            mockedFirebase.close();
        }
        UserInSession.clear();
    }

    @Test
    public void onCreate_fetchEvents_populatesRecyclerView() {
        DataSnapshot checkSnapshot = mock(DataSnapshot.class);
        when(checkSnapshot.exists()).thenReturn(true);
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(checkSnapshot);
            return null;
        }).when(eventsRef).addListenerForSingleValueEvent(any());

        EventItem event = new EventItem("SOEN Mixer", "March 20", "EV Lobby", "Networking");
        DataSnapshot eventChild = mock(DataSnapshot.class);
        when(eventChild.getValue(EventItem.class)).thenReturn(event);

        DataSnapshot eventsSnapshot = mock(DataSnapshot.class);
        when(eventsSnapshot.getChildren()).thenReturn(Collections.singletonList(eventChild));
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(eventsSnapshot);
            return null;
        }).when(eventsRef).addValueEventListener(any());

        EventsPageActivity activity = Robolectric.buildActivity(EventsPageActivity.class)
                .setup()
                .get();

        RecyclerView recyclerView = activity.findViewById(R.id.rvEvents);
        assertNotNull(recyclerView.getAdapter());
        assertEquals(1, recyclerView.getAdapter().getItemCount());
    }

    @Test
    public void onCreate_whenDatabaseEmpty_uploadsSampleEvents() {
        DataSnapshot emptySnapshot = mock(DataSnapshot.class);
        when(emptySnapshot.exists()).thenReturn(false);
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(emptySnapshot);
            return null;
        }).when(eventsRef).addListenerForSingleValueEvent(any());

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            DataSnapshot snapshot = mock(DataSnapshot.class);
            when(snapshot.getChildren()).thenReturn(Collections.emptyList());
            listener.onDataChange(snapshot);
            return null;
        }).when(eventsRef).addValueEventListener(any());

        DatabaseReference pushedRef = mock(DatabaseReference.class);
        when(eventsRef.push()).thenReturn(pushedRef);
        when(pushedRef.setValue(any())).thenReturn(null);

        Robolectric.buildActivity(EventsPageActivity.class).setup().get();

        verify(eventsRef, atLeastOnce()).push();
    }
}
