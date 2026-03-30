package com.example.popin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mockito.MockedStatic;

import java.util.Collections;

public class FirebaseTestAssistant {

    private static MockedStatic<FirebaseDatabase> mockedFirebaseDatabase;

    private static MockedStatic<FirebaseDatabase> mockFirebaseDatabase;

    public static void setupMockFirebase() {

        if (mockedFirebaseDatabase != null) {
            tearDown();
        }

        FirebaseDatabase mockDb = mock(FirebaseDatabase.class);
        DatabaseReference mockRef = mock(DatabaseReference.class);
        DataSnapshot mockSnapshot = mock(DataSnapshot.class);


        when(mockDb.getReference(anyString())).thenAnswer(invocation -> {
            when(mockRef.child(anyString())).thenAnswer(childInvocation -> mockRef);
            return mockRef;
        });
        when(mockRef.getRef()).thenReturn(mockRef);

        when(mockDb.getReference(anyString())).thenAnswer(
                invocation -> mockRef
        );
        when(mockRef.addValueEventListener(any())).thenReturn(mock(ValueEventListener.class));

        when(mockRef.setValue(any())).thenReturn(mock(Task.class));
        when(mockRef.updateChildren(any())).thenReturn(mock(Task.class));
        when(mockRef.removeValue()).thenReturn(mock(Task.class));

        when(mockSnapshot.exists()).thenReturn(false);
        when(mockSnapshot.getChildren()).thenReturn(Collections.emptyList());


        mockedFirebaseDatabase = mockStatic(FirebaseDatabase.class);
        mockedFirebaseDatabase.when(FirebaseDatabase::getInstance).thenReturn(mockDb);


    }

    public static void tearDown() {
        if (mockedFirebaseDatabase != null) {
            mockedFirebaseDatabase.close();
            mockedFirebaseDatabase = null;
        }
        if (mockFirebaseDatabase != null) {
            mockFirebaseDatabase.close();
            mockFirebaseDatabase = null;
        }
    }

    public static MockedStatic<FirebaseDatabase> getMockedDatabase() {
        return mockedFirebaseDatabase;
    }

}
