package com.example.popin.logicUnitTests;



import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.popin.logic.User;
import com.example.popin.logic.UserInSession;

public class UserInSessionTest {

    @After
    public void tearDown() {
        UserInSession.clear();
    }


    @Test
    public void userInSessionCreateAffectsGetInstanceAndGetUser(){
        User u = new User("hos", "sam");

        UserInSession.create(u);
        assertEquals(u, UserInSession.getInstance().getUser());

    }

    @Test
    public void userInSessionCanChange(){

        User u = new User("hos", "sam");

        UserInSession.create(u);
        assertEquals(u, UserInSession.getInstance().getUser());

        UserInSession.clear();
        assertNull(UserInSession.getInstance());

        User u2 = new User("TESTMAN", "testing");

        UserInSession.create(u2);
        assertEquals(u2, UserInSession.getInstance().getUser());

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

