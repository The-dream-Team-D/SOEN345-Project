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

import com.example.popin.logic.GenericCallback;
import com.example.popin.logic.Notifications;
import com.example.popin.logic.User;
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

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class UserAccountFlowTest {

    private MockedStatic<FirebaseDatabase> mockedFirebase;
    private MockedStatic<Notifications> mockedNotifications;

    private FirebaseDatabase mockDb;
    private DatabaseReference usersRef;
    private Query emailQuery;
    private Query phoneQuery;
    private DataSnapshot snapshot;
    private DataSnapshot userSnapshot;

    @Before
    public void setUp() {
        mockDb = mock(FirebaseDatabase.class);
        usersRef = mock(DatabaseReference.class);
        emailQuery = mock(Query.class);
        phoneQuery = mock(Query.class);
        snapshot = mock(DataSnapshot.class);
        userSnapshot = mock(DataSnapshot.class);

        when(mockDb.getReference("Users")).thenReturn(usersRef);
        when(usersRef.orderByChild("email")).thenReturn(emailQuery);
        when(usersRef.orderByChild("phoneNumber")).thenReturn(phoneQuery);
        when(emailQuery.equalTo(anyString())).thenReturn(emailQuery);
        when(phoneQuery.equalTo(anyString())).thenReturn(phoneQuery);

        mockedFirebase = mockStatic(FirebaseDatabase.class);
        mockedFirebase.when(FirebaseDatabase::getInstance).thenReturn(mockDb);
        mockedNotifications = mockStatic(Notifications.class);
    }

    @After
    public void tearDown() {
        if (mockedNotifications != null) {
            mockedNotifications.close();
        }
        if (mockedFirebase != null) {
            mockedFirebase.close();
        }
    }

    private void callbackSnapshot(Query query, DataSnapshot targetSnapshot) {
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(targetSnapshot);
            return null;
        }).when(query).addListenerForSingleValueEvent(any(ValueEventListener.class));
    }

    @Test
    public void signUp_emptyName_returnsValidationError() {
        AtomicReference<String> error = new AtomicReference<>();

        User.SignUp("", "user@example.com", "pw", new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected validation error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Name input is empty", error.get());
    }

    @Test
    public void signUp_duplicateUser_returnsError() {
        when(snapshot.exists()).thenReturn(true);
        callbackSnapshot(emailQuery, snapshot);
        AtomicReference<String> error = new AtomicReference<>();

        User.SignUp("Kevin", "user@example.com", "pw", new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected duplicate user error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("An account with this Email/Phone Number already exists", error.get());
    }

    @Test
    public void forgotPassword_noUser_returnsError() {
        when(snapshot.exists()).thenReturn(false);
        callbackSnapshot(emailQuery, snapshot);
        AtomicReference<String> error = new AtomicReference<>();

        User.forgotPassword("user@example.com", "newPw", new User.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                fail("Expected no-user error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("No user with that email/phone number", error.get());
    }

    @Test
    public void forgotPassword_samePassword_returnsError() {
        DataSnapshot pwdSnapshot = mock(DataSnapshot.class);
        when(snapshot.exists()).thenReturn(true);
        when(snapshot.getChildren()).thenReturn(Collections.singletonList(userSnapshot));
        when(userSnapshot.child("password")).thenReturn(pwdSnapshot);
        when(pwdSnapshot.getValue(String.class)).thenReturn("samePw");
        callbackSnapshot(emailQuery, snapshot);

        AtomicReference<String> error = new AtomicReference<>();
        User.forgotPassword("user@example.com", "samePw", new User.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                fail("Expected error for unchanged password");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("New password can't be the same as old password", error.get());
    }

    @Test
    public void changePassword_userMissing_returnsError() {
        when(snapshot.exists()).thenReturn(false);
        callbackSnapshot(emailQuery, snapshot);

        User user = User.createUserWithEmail("user@example.com", "newPass");
        AtomicReference<String> error = new AtomicReference<>();

        user.changePassword(new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected missing-user error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("No user with that email/phone number", error.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void changePassword_success_callsSuccessCallback() {
        DataSnapshot passwordSnapshot = mock(DataSnapshot.class);
        DatabaseReference userRef = mock(DatabaseReference.class);
        DatabaseReference passwordRef = mock(DatabaseReference.class);
        Task<Void> task = mock(Task.class);

        when(snapshot.exists()).thenReturn(true);
        when(snapshot.getChildren()).thenReturn(Collections.singletonList(userSnapshot));
        when(userSnapshot.getRef()).thenReturn(userRef);
        when(userRef.child("password")).thenReturn(passwordRef);
        when(passwordRef.setValue(any())).thenReturn(task);
        when(task.addOnSuccessListener(any())).thenAnswer(invocation -> {
            com.google.android.gms.tasks.OnSuccessListener<Void> listener = invocation.getArgument(0);
            listener.onSuccess(null);
            return task;
        });
        when(task.addOnFailureListener(any())).thenReturn(task);
        when(userSnapshot.child("password")).thenReturn(passwordSnapshot);
        callbackSnapshot(emailQuery, snapshot);

        User user = User.createUserWithEmail("user@example.com", "newPass");
        AtomicReference<String> success = new AtomicReference<>();

        user.changePassword(new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                success.set(message);
            }

            @Override
            public void onError(String message) {
                fail("Expected success");
            }
        });

        assertEquals("Password Changed Successfully!", success.get());
    }

    @Test
    public void updateProfile_userNotFound_returnsError() {
        when(snapshot.exists()).thenReturn(false);
        callbackSnapshot(emailQuery, snapshot);

        User user = User.createUserWithEmail("user@example.com", "pw");
        AtomicReference<String> error = new AtomicReference<>();

        user.updateProfile(new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected user not found");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("User not found", error.get());
    }

    @Test
    public void delete_withMissingUser_returnsError() {
        when(snapshot.exists()).thenReturn(false);
        callbackSnapshot(emailQuery, snapshot);
        AtomicReference<String> error = new AtomicReference<>();

        User user = User.createUserWithEmail("user@example.com", "pw");
        user.delete(new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected delete error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Error encountered locating user in DB", error.get());
    }

    @Test
    public void delete_withBlankEmail_usesPhoneQueryBranch() {
        when(snapshot.exists()).thenReturn(false);
        callbackSnapshot(phoneQuery, snapshot);
        User user = User.createUserWithPhoneNumber("+15145551234", "pw");

        user.delete(new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected error path");
            }

            @Override
            public void onError(String message) {
                assertEquals("Error encountered locating user in DB", message);
            }
        });

        verify(usersRef).orderByChild("phoneNumber");
    }

    @Test
    public void signUp_queryCancelled_returnsDatabaseError() {
        DatabaseError dbError = mock(DatabaseError.class);
        when(dbError.getMessage()).thenReturn("permission-denied");
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onCancelled(dbError);
            return null;
        }).when(emailQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        AtomicReference<String> error = new AtomicReference<>();
        User.SignUp("Kevin", "user@example.com", "pw", new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected database error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Database error: permission-denied", error.get());
    }
}
