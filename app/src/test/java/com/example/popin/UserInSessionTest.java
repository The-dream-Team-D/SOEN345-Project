package com.example.popin;



import org.junit.Test;

import static org.junit.Assert.*;

public class UserInSessionTest {

    private NavBarComponentView navBar;

    @Test
    public void userInSessionCreateAffectsGetInstanceAndGetUser(){
        User u = new User("hos", "sam");

        UserInSession.create(u);
        assertEquals(u, UserInSession.getInstance().getUser());
        assertEquals(u, UserInSession.getCurrentUser());
    }

    @Test
    public void getCurrentUser_whenNoSession_returnsNull() {
        UserInSession.clear();
        assertNull(UserInSession.getCurrentUser());
    }

    @Test
    public void getCurrentUser_afterCreate_returnsSameUser() {
        UserInSession.clear();
        User u = new User("e@e.com", "p");
        UserInSession.create(u);
        assertEquals(u, UserInSession.getCurrentUser());
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
