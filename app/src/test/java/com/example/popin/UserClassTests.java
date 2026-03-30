package com.example.popin;

import com.google.firebase.database.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserClassTests {
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
    
    private MockedStatic<FirebaseDatabase> mockedFirebase;

    // Simulate the one existing user in the DB
    private static final String email_in_DB = "john@example.com";
    private static final String password_in_DB = "secret123";
    private static final String name_in_DB     = "John Doe";
    private static final String address_in_DB = "123 Main St";

    @Captor
    ArgumentCaptor<ValueEventListener> listenerCaptor;

    @Before
    public void setUp() {

        MockitoAnnotations.openMocks(this); // make sure this is here

        mockedFirebase = mockStatic(FirebaseDatabase.class);
        mockedFirebase.when(FirebaseDatabase::getInstance).thenReturn(mockFirebaseDatabase);

        when(mockFirebaseDatabase.getReference("Users")).thenReturn(mockUsersRef);
        when(mockUsersRef.orderByChild("email")).thenReturn(mockQuery);
        when(mockQuery.equalTo(anyString())).thenReturn(mockQuery);

        DataSnapshot mockPasswordSnapshot = mock(DataSnapshot.class);
        DataSnapshot mockNameSnapshot     = mock(DataSnapshot.class);
        DataSnapshot mockAddressSnapshot  = mock(DataSnapshot.class);
        DataSnapshot mockIsAdminSnapshot  = mock(DataSnapshot.class);

        when(mockUserSnapshot.child("password")).thenReturn(mockPasswordSnapshot);
        when(mockUserSnapshot.child("name")).thenReturn(mockNameSnapshot);
        when(mockUserSnapshot.child("address")).thenReturn(mockAddressSnapshot);
        when(mockUserSnapshot.child("isAdmin")).thenReturn(mockIsAdminSnapshot);

        when(mockPasswordSnapshot.getValue(String.class)).thenReturn(password_in_DB);
        when(mockNameSnapshot.getValue(String.class)).thenReturn(name_in_DB);
        when(mockAddressSnapshot.getValue(String.class)).thenReturn(address_in_DB);
        when(mockIsAdminSnapshot.getValue(boolean.class)).thenReturn(false);

    }


    @org.junit.After
    public void tearDown() {
        if (mockedFirebase != null) mockedFirebase.close();
    }

    private void setupSnapshotExists() {
        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getChildren()).thenReturn(java.util.Collections.singletonList(mockUserSnapshot));

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));
    }

    private void setupSnapshotNotExists() {
        when(mockSnapshot.exists()).thenReturn(false);

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));
    }

    // --- Core Logic Tests (Firebase) ---

    @Test
    public void login_emptyEmail_returnsError() {
        User user = new User("", "anyPassword");
        user.login(new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Expected error, got success"); }
            @Override public void onError(String message) {
                assertEquals("Email/Phone input is Empty", message);
            }
        });
    }

    @Test
    public void login_nullEmail_returnsError() {
        User user = new User(null, "anyPassword");
        user.login(new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Expected error, got success"); }
            @Override public void onError(String message) {
                assertEquals("Email/Phone input is Empty", message);
            }
        });
    }

    @Test
    public void login_emptyPassword_returnsError() {
        User user = new User(email_in_DB, "");
        user.login(new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Expected error, got success"); }
            @Override public void onError(String message) {
                assertEquals("Password input is Empty", message);
            }
        });
    }

    @Test
    public void login_nullPassword_returnsError() {
        User user = new User(email_in_DB, null);
        user.login(new User.LoginCallback() {
            @Override public void onSuccess(User u) {
                fail("Expected error, got success");
            }
            @Override public void onError(String message) {
                assertEquals("Password input is Empty", message);
            }
        });
    }

    @Test
    public void login_correctCredentials_callsOnSuccess() {
        setupSnapshotExists();
        User user = new User(email_in_DB, password_in_DB);

        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getChildren()).thenReturn(List.of(mockUserSnapshot));

        user.login(new User.LoginCallback() {
            @Override public void onSuccess(User u) {
                UserInSession.create(user);
                assertNotNull(u);
                assertEquals(name_in_DB,    u.getName());
                assertEquals(address_in_DB, u.getAddress());
                assertEquals(email_in_DB,   u.getEmail());
                assertNotNull(UserInSession.getInstance().getUser());
            }
            @Override public void onError(String message) {
                fail("Expected success, got error: " + message);
            }
        });

        verify(mockQuery).addListenerForSingleValueEvent(listenerCaptor.capture());
        listenerCaptor.getValue().onDataChange(mockSnapshot);

    }

    @Test
    public void login_wrongPassword_callsOnError() {
        setupSnapshotExists();
        User user = new User(email_in_DB, "wrongPassword");

        user.login(new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Expected error, got success"); }
            @Override public void onError(String message) {
                assertNull(UserInSession.getInstance());
                assertEquals("Incorrect password", message);
            }
        });
    }

    @Test
    public void login_emailNotFound_callsOnError() {
        setupSnapshotNotExists();
        User user = new User("nobody@example.com", password_in_DB);

        user.login(new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Expected error, got success"); }
            @Override public void onError(String message) {
                assertEquals("No user with that email/phone number", message);
            }
        });
    }

    @Test
    public void login_databaseCancelled_logsError() {
        DatabaseError mockError = mock(DatabaseError.class);
        when(mockError.getMessage()).thenReturn("Connection lost");

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onCancelled(mockError);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        User user = new User(email_in_DB, password_in_DB);

        user.login(new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Should not succeed on cancel"); }
            @Override public void onError(String msg)  { fail("onError should not be called on cancel"); }
        });

        verify(mockError).getMessage();
    }

    // --- Simple Data Tests (Getters/Setters) ---

    @Test
    public void testSettersAndGetters() {
        User user = new User("test@example.com", "pass123");
        
        user.setName("John Smith");
        user.setAddress("456 Oak St");
        user.setPassword("newPass456");

        assertEquals("John Smith", user.getName());
        assertEquals("456 Oak St", user.getAddress());
        assertEquals("newPass456", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testEmptyConstructor() {
        User user = new User();
        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }
}
