package com.example.popin.logicUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.popin.logic.User;
import com.example.popin.logic.UserInSession;

import org.junit.After;
import org.junit.Test;

public class UserInSessionExtraTest {

    @After
    public void tearDown() {
        UserInSession.clear();
    }

    @Test
    public void updateUser_withoutInstance_doesNothing() {
        UserInSession.clear();

        UserInSession.updateUser(new User("user@example.com", "pw"));

        assertNull(UserInSession.getInstance());
    }

    @Test
    public void updateUser_withInstance_replacesCurrentUser() {
        User first = new User("first@example.com", "pw1");
        User second = new User("second@example.com", "pw2");

        UserInSession.create(first);
        UserInSession.updateUser(second);

        assertEquals(second, UserInSession.getInstance().getUser());
    }
}

