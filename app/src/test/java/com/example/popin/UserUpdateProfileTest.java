package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class UserUpdateProfileTest {

    @Mock
    private FirebaseDatabase mockFirebaseDatabase;
    @Mock
    private DatabaseReference mockUsersRef;
    @Mock
    private Query mockQuery;
    @Mock
    private DataSnapshot mockSnapshot;
    @Mock
    private DataSnapshot mockUserSnapshot;
    @Mock
    private DatabaseReference mockUserRef;
    @Mock
    private DatabaseReference mockNameRef;
    @Mock
    private DatabaseReference mockAddressRef;
    @Mock
    private DatabaseReference mockPhoneRef;
    @Mock
    private DatabaseReference mockBioRef;

    private MockedStatic<FirebaseDatabase> mockedFirebase;

    @Before
    public void setUp() {
        mockedFirebase = mockStatic(FirebaseDatabase.class);
        mockedFirebase.when(FirebaseDatabase::getInstance).thenReturn(mockFirebaseDatabase);

        when(mockFirebaseDatabase.getReference("Users")).thenReturn(mockUsersRef);
        when(mockUsersRef.orderByChild("email")).thenReturn(mockQuery);
        when(mockQuery.equalTo(anyString())).thenReturn(mockQuery);

        when(mockUserSnapshot.getRef()).thenReturn(mockUserRef);
        when(mockUserRef.child("name")).thenReturn(mockNameRef);
        when(mockUserRef.child("address")).thenReturn(mockAddressRef);
        when(mockUserRef.child("phone")).thenReturn(mockPhoneRef);
        when(mockUserRef.child("bio")).thenReturn(mockBioRef);

        Task<Void> mockTask = mock(Task.class);
        when(mockNameRef.setValue(any())).thenReturn(mockTask);
        when(mockAddressRef.setValue(any())).thenReturn(mockTask);
        when(mockPhoneRef.setValue(any())).thenReturn(mockTask);
        when(mockBioRef.setValue(any())).thenReturn(mockTask);
    }

    @After
    public void tearDown() {
        if (mockedFirebase != null) {
            mockedFirebase.close();
        }
    }

    @Test
    public void updateProfile_userExists_updatesAndCallsSuccess() {
        User user = new User("john@example.com", "pass");
        user.setName("John Doe");
        user.setAddress("123 Main St");
        user.setPhone("5140000000");
        user.setBio("SOEN student");

        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getChildren()).thenReturn(Collections.singletonList(mockUserSnapshot));
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        user.updateProfile(new User.UpdateCallback() {
            @Override
            public void onSuccess() {
                verify(mockNameRef).setValue("John Doe");
                verify(mockAddressRef).setValue("123 Main St");
                verify(mockPhoneRef).setValue("5140000000");
                verify(mockBioRef).setValue("SOEN student");
            }

            @Override
            public void onError(String message) {
                fail("Expected success, got: " + message);
            }
        });
    }

    @Test
    public void updateProfile_userMissing_callsError() {
        User user = new User("missing@example.com", "pass");
        when(mockSnapshot.exists()).thenReturn(false);

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        user.updateProfile(new User.UpdateCallback() {
            @Override
            public void onSuccess() {
                fail("Expected error");
            }

            @Override
            public void onError(String message) {
                assertEquals("User not found", message);
            }
        });
    }

    @Test
    public void updateProfile_databaseCancelled_returnsErrorMessage() {
        User user = new User("john@example.com", "pass");
        DatabaseError error = mock(DatabaseError.class);
        when(error.getMessage()).thenReturn("network down");

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onCancelled(error);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        user.updateProfile(new User.UpdateCallback() {
            @Override
            public void onSuccess() {
                fail("Expected error");
            }

            @Override
            public void onError(String message) {
                assertEquals("network down", message);
            }
        });
    }
}
