package com.example.popin.logicUnitTests;



import org.junit.Test;

import static org.junit.Assert.*;

import com.example.popin.logic.User;
import com.example.popin.logic.UserInSession;

public class UserInSessionTest {

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

}

