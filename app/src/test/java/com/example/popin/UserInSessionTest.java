package com.example.popin;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserInSessionTest {

    @Before
    public void setUp() {
        UserInSession.clear();
    }

    @Test
    public void create_setsCurrentUser() {
        User user = new User("test@example.com", "pass");
        UserInSession.create(user);
        assertNotNull(UserInSession.getInstance());
        assertEquals(user, UserInSession.getInstance().getUser());
    }

    @Test
    public void clear_removesSession() {
        UserInSession.create(new User());
        UserInSession.clear();
        assertNull(UserInSession.getInstance());
    }
}
