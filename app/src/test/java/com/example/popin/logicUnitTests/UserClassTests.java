package com.example.popin.logicUnitTests;

import com.example.popin.logic.NotificationPreferenceOptions;
import com.example.popin.logic.User;
import com.example.popin.logic.UserInSession;
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


    private static final String EMAIL_IN_DB = "john@example.com";
    private static final String PASSWORD_IN_DB = "secret123";
    private static final String NAME_IN_DB     = "John Doe";
    private static final String ADDRESS_IN_DB = "123 Main St";
    private static final String PHONE_NUMBER_IN_DB = "+15141234567";
    private static final NotificationPreferenceOptions NOTIF_PREF_IN_DB = NotificationPreferenceOptions.EMAIL;
    private static final String BIO_IN_DB = "Hello I am a user for tests";

    private static final boolean IS_ADMIN_IN_DB = false;

    @Captor
    ArgumentCaptor<ValueEventListener> listenerCaptor;

    @Before
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        mockedFirebase = mockStatic(FirebaseDatabase.class);
        mockedFirebase.when(FirebaseDatabase::getInstance).thenReturn(mockFirebaseDatabase);

        when(mockFirebaseDatabase.getReference("Users")).thenReturn(mockUsersRef);
        when(mockUsersRef.orderByChild("email")).thenReturn(mockQuery);
        when(mockQuery.equalTo(anyString())).thenReturn(mockQuery);

        DataSnapshot mockPasswordSnapshot = mock(DataSnapshot.class);
        DataSnapshot mockNameSnapshot     = mock(DataSnapshot.class);
        DataSnapshot mockAddressSnapshot  = mock(DataSnapshot.class);
        DataSnapshot mockIsAdminSnapshot  = mock(DataSnapshot.class);
        DataSnapshot mockPhoneSnapshot = mock(DataSnapshot.class);
        DataSnapshot mockPrefSnapshot  = mock(DataSnapshot.class);
        DataSnapshot mockBioSnapshot  = mock(DataSnapshot.class);

        lenient().when(mockUserSnapshot.child("password")).thenReturn(mockPasswordSnapshot);
        lenient().when(mockUserSnapshot.child("name")).thenReturn(mockNameSnapshot);
        lenient().when(mockUserSnapshot.child("address")).thenReturn(mockAddressSnapshot);
        lenient().when(mockUserSnapshot.child("isAdmin")).thenReturn(mockIsAdminSnapshot);
        lenient().when(mockUserSnapshot.child("phoneNumber")).thenReturn(mockPhoneSnapshot);
        lenient().when(mockUserSnapshot.child("NotificationPreference")).thenReturn(mockPrefSnapshot);
        lenient().when(mockUserSnapshot.child("bio")).thenReturn(mockBioSnapshot);

        lenient().when(mockPasswordSnapshot.getValue(String.class)).thenReturn(PASSWORD_IN_DB);
        lenient().when(mockNameSnapshot.getValue(String.class)).thenReturn(NAME_IN_DB);
        lenient().when(mockAddressSnapshot.getValue(String.class)).thenReturn(ADDRESS_IN_DB);
        lenient().when(mockPhoneSnapshot.getValue(String.class)).thenReturn(PHONE_NUMBER_IN_DB);

        lenient().when(mockPrefSnapshot.getValue(String.class))
                .thenReturn(NOTIF_PREF_IN_DB.name());

        lenient().when(mockBioSnapshot.getValue(String.class)).thenReturn(BIO_IN_DB);
        lenient().when(mockIsAdminSnapshot.getValue(boolean.class)).thenReturn(IS_ADMIN_IN_DB);

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



    @Test
    public void login_emptyEmail_returnsError() {
        User user = new User("", "anyPassword");
        User.login(user.getEmail(), user.getPassword(), new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Expected error, got success"); }
            @Override public void onError(String message) {
                assertEquals("Email/Phone input is Empty", message);
            }
        });
    }

    @Test
    public void login_nullEmail_returnsError() {
        User user = new User(null, "anyPassword");
        User.login(user.getEmail(), user.getPassword(), new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Expected error, got success"); }
            @Override public void onError(String message) {
                assertEquals("Email/Phone input is Empty", message);
            }
        });
    }

    @Test
    public void login_emptyPassword_returnsError() {
        User user = new User(EMAIL_IN_DB, "");
        User.login(user.getEmail(), user.getPassword(), new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Expected error, got success"); }
            @Override public void onError(String message) {
                assertEquals("Password input is Empty", message);
            }
        });
    }

    @Test
    public void login_nullPassword_returnsError() {
        User user = new User(EMAIL_IN_DB, null);
        User.login(user.getEmail(), user.getPassword(), new User.LoginCallback() {
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
        User user = new User(EMAIL_IN_DB, PASSWORD_IN_DB);

        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getChildren()).thenReturn(List.of(mockUserSnapshot));

        User.login(user.getEmail(), user.getPassword(), new User.LoginCallback() {
            @Override public void onSuccess(User u) {
                UserInSession.create(user);
                assertNotNull(u);
                assertEquals(NAME_IN_DB, u.getName());
                assertEquals(ADDRESS_IN_DB, u.getAddress());
                assertEquals(EMAIL_IN_DB, u.getEmail());
                assertEquals(IS_ADMIN_IN_DB, u.getIsAdmin());
                assertNotNull(UserInSession.getInstance().getUser());
            }
            @Override public void onError(String message) {
                fail("Expected success, got error: " + message);
            }
        });
    }

    @Test
    public void login_wrongPassword_callsOnError() {
        setupSnapshotExists();
        User user = new User(EMAIL_IN_DB, "wrongPassword");

        User.login(user.getEmail(), user.getPassword(), new User.LoginCallback() {
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
        User user = new User("nobody@example.com", PASSWORD_IN_DB);

        User.login(user.getEmail(), user.getPassword(), new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Expected error, got success"); }
            @Override public void onError(String message) {
                assertEquals("No user with that email/phone number", message);
            }
        });
    }

    @Test
    public void login_databaseCancelled_callsOnError() {
        DatabaseError mockError = mock(DatabaseError.class);
        when(mockError.getMessage()).thenReturn("Connection lost");

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onCancelled(mockError);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        User user = new User(EMAIL_IN_DB, PASSWORD_IN_DB);

        User.login(user.getEmail(), user.getPassword(), new User.LoginCallback() {
            @Override public void onSuccess(User u) { fail("Should not succeed on cancel"); }
            @Override public void onError(String msg)  { assertEquals("Database error: Connection lost", msg); }
        });

        verify(mockError).getMessage();
    }



    @Test
    public void testSettersAndGetters() {
        User user = new User("test@example.com", "pass123");

        user.setName("John Smith");
        user.setAddress("456 Oak St");
        user.setPassword("newPass456");
        user.setPhoneNumber("5141234567");
        user.setBio("Hello I am John");
        user.setIsAdmin(true);
        user.setUserNotificationPreference(NotificationPreferenceOptions.EMAIL);

        assertEquals("John Smith", user.getName());
        assertEquals("456 Oak St", user.getAddress());
        assertEquals("newPass456", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("5141234567", user.getPhoneNumber());
        assertEquals("Hello I am John", user.getBio());
        assertTrue(user.getIsAdmin());
        assertEquals(NotificationPreferenceOptions.EMAIL, user.getUserNotificationPreference());
    }

    @Test
    public void testEmptyConstructor() {
        User user = new User();
        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }
}

