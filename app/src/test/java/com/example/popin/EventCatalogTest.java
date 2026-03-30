package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(MockitoJUnitRunner.class)
public class EventCatalogTest {

    @Mock
    private FirebaseDatabase mockDb;
    @Mock
    private DatabaseReference eventsRef;
    @Mock
    private DatabaseReference childRef;

    private MockedStatic<FirebaseDatabase> firebaseStatic;

    @Before
    public void setUp() {
        EventCatalog.clearInstanceForTests();
        firebaseStatic = mockStatic(FirebaseDatabase.class);
        firebaseStatic.when(FirebaseDatabase::getInstance).thenReturn(mockDb);
        when(mockDb.getReference("Events")).thenReturn(eventsRef);
        when(eventsRef.child(anyString())).thenReturn(childRef);
    }

    @After
    public void tearDown() {
        EventCatalog.clearInstanceForTests();
        if (firebaseStatic != null) {
            firebaseStatic.close();
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Task<T> immediateSuccessTask() {
        Task<T> task = mock(Task.class);
        doAnswer(invocation -> {
            OnSuccessListener<T> listener = invocation.getArgument(0);
            listener.onSuccess(null);
            return task;
        }).when(task).addOnSuccessListener(any(OnSuccessListener.class));
        when(task.addOnFailureListener(any(OnFailureListener.class))).thenReturn(task);
        return task;
    }

    @Test
    public void addEvent_validEvent_invokesSuccessCallback() {
        Task<Void> successTask = immediateSuccessTask();

        when(childRef.setValue(any())).thenReturn(successTask);
        
        EventCatalog catalog = EventCatalog.getInstance();
        Event event = new Event("Concert", "Hall A", "Live music", new Date(), EventCategory.CONCERT);

        AtomicReference<String> result = new AtomicReference<>();
        catalog.addEvent(event, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                result.set(message);
            }

            @Override
            public void onError(String message) {
                throw new AssertionError("Unexpected error: " + message);
            }
        });

        assertEquals("Event added successfully", result.get());
        verify(childRef).setValue(any(Event.class));
    }

    @Test
    public void addEvent_nullEvent_reportsError() {
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();
        catalog.addEvent(null, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                throw new AssertionError("Expected error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });
        assertEquals("Event is null", error.get());
    }

    @Test
    public void addEvent_emptyName_reportsError() {
        EventCatalog catalog = EventCatalog.getInstance();
        Event event = new Event("  ", "L", "D", new Date(), EventCategory.CONCERT);
        AtomicReference<String> error = new AtomicReference<>();
        catalog.addEvent(event, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                throw new AssertionError("Expected error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });
        assertEquals("Event name is empty", error.get());
    }

    @Test
    public void updateEventByName_emptyName_reportsError() {
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();
        catalog.updateEventByName(
                "",
                "New",
                "Loc",
                "Desc",
                new Date(),
                EventCategory.CONCERT,
                null,
                new EventCatalog.EventActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        throw new AssertionError("Expected error");
                    }

                    @Override
                    public void onError(String message) {
                        error.set(message);
                    }
                });
        assertEquals("Event name is empty", error.get());
    }

    @Test
    public void deleteEventByName_emptyName_reportsError() {
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();
        catalog.deleteEventByName("  ", new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                throw new AssertionError("Expected error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });
        assertEquals("Event name is empty", error.get());
    }
}
