package com.example.popin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.mockito.MockedStatic;

import java.util.Collections;

public class FirebaseTestAssistant {

    private static MockedStatic<FirebaseDatabase> mockedFirebaseDatabase;

    public static void setupMockFirebase() {
        tearDown();

        FirebaseDatabase mockDb = mock(FirebaseDatabase.class);
        DatabaseReference mockRef = mock(DatabaseReference.class);
        Query mockQuery = mock(Query.class);
        DataSnapshot mockSnapshot = mock(DataSnapshot.class);

        when(mockDb.getReference(anyString())).thenReturn(mockRef);
        when(mockRef.child(anyString())).thenReturn(mockRef);
        when(mockRef.getRef()).thenReturn(mockRef);

        when(mockRef.orderByChild(anyString())).thenReturn(mockQuery);
        when(mockQuery.equalTo(any())).thenReturn(mockQuery);

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        when(mockRef.addValueEventListener(any(ValueEventListener.class)))
                .thenReturn(mock(ValueEventListener.class));

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
    }
}