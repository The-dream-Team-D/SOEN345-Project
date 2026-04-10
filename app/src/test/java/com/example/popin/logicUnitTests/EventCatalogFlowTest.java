package com.example.popin.logicUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.media.metrics.Event;

import com.example.popin.logic.EventCatalog;
import com.example.popin.logic.EventCategory;
import com.example.popin.logic.EventItem;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class EventCatalogFlowTest {
    private MockedStatic<FirebaseDatabase> mockedFirebase;
    private FirebaseDatabase db;
    private DatabaseReference eventsRef;
    private DatabaseReference pushedRef;
    private DatabaseReference eventRef;
    private Query query;
    private DataSnapshot snapshot;
    private DataSnapshot eventSnapshot;

    @Before
    public void setUp() throws Exception {
        resetSingleton();
        db = mock(FirebaseDatabase.class);
        eventsRef = mock(DatabaseReference.class);
        pushedRef = mock(DatabaseReference.class);
        eventRef = mock(DatabaseReference.class);
        query = mock(Query.class);
        snapshot = mock(DataSnapshot.class);
        eventSnapshot = mock(DataSnapshot.class);

        when(db.getReference("Event database")).thenReturn(eventsRef);
        when(eventsRef.push()).thenReturn(pushedRef);
        when(eventsRef.orderByChild(anyString())).thenReturn(query);
        when(query.equalTo(any())).thenReturn(query);
        when(eventSnapshot.getRef()).thenReturn(eventRef);

        mockedFirebase = mockStatic(FirebaseDatabase.class);
        mockedFirebase.when(FirebaseDatabase::getInstance).thenReturn(db);
    }

    @After
    public void tearDown() throws Exception {
        if (mockedFirebase != null) {
            mockedFirebase.close();
        }
        resetSingleton();
    }

    private void resetSingleton() throws Exception {
        Field instanceField = EventCatalog.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    private void callbackSnapshot(Query queryToUse, DataSnapshot dataSnapshot) {
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(dataSnapshot);
            return null;
        }).when(queryToUse).addListenerForSingleValueEvent(any(ValueEventListener.class));
    }

    private EventCatalog.EventUpdateRequest request(
            String name,
            String location,
            String description,
            long date,
            EventCategory category,
            int capacity
    ) {
        EventCatalog.EventUpdateRequest request = new EventCatalog.EventUpdateRequest();
        request.setNewName(name);
        request.setNewLocation(location);
        request.setNewDescription(description);
        request.setNewDate(date);
        request.setNewCategory(category);
        request.setNewCapacity(capacity);
        return request;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addEvent_success_callsSuccessCallback() {
        Task<Void> task = mock(Task.class);
        when(pushedRef.setValue(any())).thenReturn(task);
        when(task.addOnSuccessListener(any())).thenAnswer(invocation -> {
            com.google.android.gms.tasks.OnSuccessListener<Void> listener = invocation.getArgument(0);
            listener.onSuccess(null);
            return task;
        });
        when(task.addOnFailureListener(any())).thenReturn(task);

        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> success = new AtomicReference<>();

        catalog.addEvent(new EventItem("SOEN Mixer", System.currentTimeMillis(), "EV"), new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                success.set(message);
            }

            @Override
            public void onError(String message) {
                fail("Expected success");
            }
        });

        assertEquals("Event added successfully", success.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addEvent_failure_callsErrorCallback() {
        Task<Void> task = mock(Task.class);
        when(pushedRef.setValue(any())).thenReturn(task);
        when(task.addOnSuccessListener(any())).thenReturn(task);
        when(task.addOnFailureListener(any())).thenAnswer(invocation -> {
            com.google.android.gms.tasks.OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new RuntimeException("write-failed"));
            return task;
        });

        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.addEvent(new EventItem("SOEN Mixer", System.currentTimeMillis(), "EV"), new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected failure");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Failed to add event: write-failed", error.get());
    }

    @Test
    public void deleteEventByName_missingEvent_returnsError() {
        when(snapshot.exists()).thenReturn(false);
        callbackSnapshot(query, snapshot);
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.deleteEventByName("NotFound", new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected missing-event error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("No event found with that name", error.get());
    }

    @Test
    public void deleteEventByName_queryCancelled_returnsDatabaseError() {
        DatabaseError dbError = mock(DatabaseError.class);
        when(dbError.getMessage()).thenReturn("cancelled");
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onCancelled(dbError);
            return null;
        }).when(query).addListenerForSingleValueEvent(any(ValueEventListener.class));

        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();
        catalog.deleteEventByName("SOEN Mixer", new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected cancellation error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Database error: cancelled", error.get());
    }

    @Test
    public void updateEventByName_noFieldsProvided_returnsError() {
        when(snapshot.exists()).thenReturn(true);
        when(snapshot.getChildren()).thenReturn(Collections.singletonList(eventSnapshot));
        callbackSnapshot(query, snapshot);
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.updateEventByName(
                "SOEN Mixer",
                request(null, "   ", "", -1, null, -1),
                new EventCatalog.EventActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        fail("Expected no-fields error");
                    }

                    @Override
                    public void onError(String message) {
                        error.set(message);
                    }
                }
        );

        assertEquals("No fields provided to update", error.get());
    }

    @Test
    public void editEventByName_existingEventNull_returnsError() {
        EventItem updatedEvent = new EventItem("New",177578034432L, "Hall", "Details");
        updatedEvent.setCategory(EventCategory.EDUCATIONAL);


        when(snapshot.exists()).thenReturn(true);
        when(snapshot.getChildren()).thenReturn(Collections.singletonList(eventSnapshot));
        when(eventSnapshot.getValue(EventItem.class)).thenReturn(null);
        callbackSnapshot(query, snapshot);

        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.editEventByName("SOEN Mixer", updatedEvent, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected read error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Failed to read event data", error.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void editEventByName_success_updatesEvent() {
        EventItem existingEvent = new EventItem("Old", 177578034432L, "Old Hall", "Old");
        existingEvent.setEventID("212341234132");
        existingEvent.setCategory(EventCategory.SOCIAL);

        EventItem updatedEvent = new EventItem("New", 177578034432L, "New Hall", "New");
        updatedEvent.setCategory(EventCategory.PROFESSIONAL);

        Task<Void> updateTask = mock(Task.class);

        when(snapshot.exists()).thenReturn(true);
        when(snapshot.getChildren()).thenReturn(Collections.singletonList(eventSnapshot));
        when(eventSnapshot.getValue(EventItem.class)).thenReturn(existingEvent);
        when(eventRef.setValue(any())).thenReturn(updateTask);
        when(updateTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
            com.google.android.gms.tasks.OnSuccessListener<Void> listener = invocation.getArgument(0);
            listener.onSuccess(null);
            return updateTask;
        });
        when(updateTask.addOnFailureListener(any())).thenReturn(updateTask);
        callbackSnapshot(query, snapshot);

        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> success = new AtomicReference<>();

        catalog.editEventByName("SOEN Mixer", updatedEvent, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                success.set(message);
            }

            @Override
            public void onError(String message) {
                fail("Expected edit success");
            }
        });

        assertEquals("Event updated successfully", success.get());
        assertEquals("212341234132", updatedEvent.getEventID());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void editEventByName_failure_returnsError() {
        EventItem existingEvent = new EventItem("Old", 1775780675572L, "Old Hall", "Old");
        existingEvent.setEventID("42");
        existingEvent.setCategory(EventCategory.SOCIAL);

        EventItem updatedEvent = new EventItem("New", 1775780675572L, "New Hall", "New");
        updatedEvent.setCategory(EventCategory.PROFESSIONAL);


        Task<Void> updateTask = mock(Task.class);

        when(snapshot.exists()).thenReturn(true);
        when(snapshot.getChildren()).thenReturn(Collections.singletonList(eventSnapshot));
        when(eventSnapshot.getValue(EventItem.class)).thenReturn(existingEvent);
        when(eventRef.setValue(any())).thenReturn(updateTask);
        when(updateTask.addOnSuccessListener(any())).thenReturn(updateTask);
        when(updateTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            com.google.android.gms.tasks.OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new RuntimeException("edit-failed"));
            return updateTask;
        });
        callbackSnapshot(query, snapshot);

        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.editEventByName("SOEN Mixer", updatedEvent, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected edit failure");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Failed to update event: edit-failed", error.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateEventByName_success_callsSuccessCallback() {
        Task<Void> updateTask = mock(Task.class);

        when(snapshot.exists()).thenReturn(true);
        when(snapshot.getChildren()).thenReturn(Collections.singletonList(eventSnapshot));
        when(eventRef.updateChildren(any())).thenReturn(updateTask);
        when(updateTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
            com.google.android.gms.tasks.OnSuccessListener<Void> listener = invocation.getArgument(0);
            listener.onSuccess(null);
            return updateTask;
        });
        when(updateTask.addOnFailureListener(any())).thenReturn(updateTask);
        callbackSnapshot(query, snapshot);

        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> success = new AtomicReference<>();

        catalog.updateEventByName(
                "SOEN Mixer",
                request(
                        "New Name",
                        "Hall A",
                        "Updated details",
                        System.currentTimeMillis(),
                        EventCategory.EDUCATIONAL,
                        150
                ),
                new EventCatalog.EventActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        success.set(message);
                    }

                    @Override
                    public void onError(String message) {
                        fail("Expected update success");
                    }
                }
        );

        assertEquals("Event updated successfully", success.get());
        verify(eventRef).updateChildren(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateEventByName_failure_returnsErrorCallback() {
        Task<Void> updateTask = mock(Task.class);

        when(snapshot.exists()).thenReturn(true);
        when(snapshot.getChildren()).thenReturn(Collections.singletonList(eventSnapshot));
        when(eventRef.updateChildren(any())).thenReturn(updateTask);
        when(updateTask.addOnSuccessListener(any())).thenReturn(updateTask);
        when(updateTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            com.google.android.gms.tasks.OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new RuntimeException("update-failed"));
            return updateTask;
        });
        callbackSnapshot(query, snapshot);

        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.updateEventByName(
                "SOEN Mixer",
                request("New Name", null, null, -1, null, -1),
                new EventCatalog.EventActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        fail("Expected update failure");
                    }

                    @Override
                    public void onError(String message) {
                        error.set(message);
                    }
                }
        );

        assertEquals("Failed to update event: update-failed", error.get());
    }

    @Test
    public void editEventByName_queryCancelled_returnsDatabaseError() {
        DatabaseError dbError = mock(DatabaseError.class);
        when(dbError.getMessage()).thenReturn("cancelled-edit");
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onCancelled(dbError);
            return null;
        }).when(query).addListenerForSingleValueEvent(any(ValueEventListener.class));

        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();
        EventItem newEvent = new EventItem("N", 177578034432L, "L", "D");
        newEvent.setCategory(EventCategory.SOCIAL);

        catalog.editEventByName("SOEN Mixer", newEvent, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected cancel error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Database error: cancelled-edit", error.get());
    }
}

