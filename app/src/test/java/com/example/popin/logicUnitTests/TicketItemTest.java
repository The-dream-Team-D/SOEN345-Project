package com.example.popin.logicUnitTests;

import static com.example.popin.FirebaseTestAssistant.setupMockFirebase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.popin.FirebaseTestAssistant;
import com.example.popin.logic.GenericCallback;
import com.example.popin.logic.TicketItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

public class TicketItemTest {
    private DatabaseReference mockRef;
    private Task<Void> mockTask;

    @Test
    public void constructor_setsTicketIdAndInheritedFields() {
        long when = System.currentTimeMillis() + 60_000;
        TicketItem item = new TicketItem("ticket-1", "SOEN Mixer", when, "EV Building Lobby");

        assertEquals("ticket-1", item.getTicketId());
        assertEquals("SOEN Mixer", item.getTitle());
        assertEquals(when, item.getDateTime());
        assertEquals("EV Building Lobby", item.getLocation());
    }

    @Test
    public void emptyConstructor_startsWithNullTicketId() {
        TicketItem item = new TicketItem();

        assertNull(item.getTicketId());
    }

    @Test
    public void setTicketId_updatesValue() {
        TicketItem item = new TicketItem();
        item.setTicketId("ticket-99");

        assertEquals("ticket-99", item.getTicketId());
    }


    // ----------------------------------- TESTS NEEDING DATABASE MOCKS

    public void setUp() {
        setupMockFirebase();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mockRef = db.getReference("anything");
        mockTask = mock(Task.class);

        when(mockRef.removeValue()).thenReturn(mockTask);
    }

    public void tearDown() {
        FirebaseTestAssistant.tearDown();
    }


    @Test
    public void cancelTicket_failure_triggersOnError() {
        setupMockFirebase();

        try {
            DatabaseReference mockRef = FirebaseDatabase.getInstance().getReference("test");
            Task<Void> mockTask = mock(Task.class);

            when(mockRef.removeValue()).thenReturn(mockTask);

            when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
            when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

            final int[] success = {0};
            final String[] errorMessage = {null};

            GenericCallback callback = new GenericCallback() {
                @Override
                public void onSuccess(String message) {
                    success[0] = 1;
                    fail("The test should not result in On Success");
                }

                @Override
                public void onError(String message) {
                    success[0] = 2;
                    errorMessage[0] = message;
                }
            };

            doAnswer(invocation -> {
                OnFailureListener listener = invocation.getArgument(0);
                listener.onFailure(new Exception("fail"));
                return mockTask;
            }).when(mockTask).addOnFailureListener(any());

            TicketItem.cancelTicket("user1", "event1", "ticket1", callback);

            assertEquals(2, success[0]);
            assertEquals("Cancellation failed.", errorMessage[0]);

        } finally {
            tearDown();
        }
    }



    @Test
    public void cancelTicket_Success_triggersOnSuccess() {
        setupMockFirebase();

        try {
            DatabaseReference mockRef = FirebaseDatabase.getInstance().getReference("test");
            Task<Void> mockTask = mock(Task.class);

            when(mockRef.removeValue()).thenReturn(mockTask);

            when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
            when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

            final int[] success = {0};
            final String[] errorMessage = {null};

            GenericCallback callback = new GenericCallback() {
                @Override
                public void onSuccess(String message) {
                    success[0] = 1;
                    errorMessage[0] = message;
                }

                @Override
                public void onError(String message) {
                    success[0] = 2;
                    fail("The test should not result in On Error");

                }
            };

            doAnswer(invocation -> {
                OnSuccessListener<Void> listener = invocation.getArgument(0);
                listener.onSuccess(null);
                return mockTask;
            }).when(mockTask).addOnSuccessListener(any());

            TicketItem.cancelTicket("user1", "event1", "ticket1", callback);

            assertEquals(1, success[0]);
            assertEquals("Ticket cancelled successfully", errorMessage[0]);

        } finally {
            tearDown();
        }
    }
}

