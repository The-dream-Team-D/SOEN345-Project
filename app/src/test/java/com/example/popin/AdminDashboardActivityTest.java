package com.example.popin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Admin dashboard only needs a session user check on create; Firebase is not touched until
 * add/update/delete actions, so these tests avoid static Firebase mocks that break under some JDKs.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class AdminDashboardActivityTest {

    @Before
    public void setUp() {
        UserInSession.clear();
        EventCatalog.clearInstanceForTests();
    }

    @After
    public void tearDown() {
        UserInSession.clear();
        EventCatalog.clearInstanceForTests();
    }

    @Test
    public void onCreate_withAdminInSession_staysOpenAndShowsControls() {
        Admin admin = new Admin("admin@test.com", "pw", "user-key-99");
        admin.setName("Admin User");
        admin.setAddress("Campus");
        admin.setIsAdmin(true);
        UserInSession.create(admin);

        AdminDashboardActivity activity =
                Robolectric.buildActivity(AdminDashboardActivity.class).create().get();

        assertFalse(activity.isFinishing());
        assertNotNull(activity.findViewById(R.id.addEvent));
        assertNotNull(activity.findViewById(R.id.button3));
        assertNotNull(activity.findViewById(R.id.button2));
    }

    @Test
    public void onCreate_withRegularUser_finishes() {
        User user = new User("user@test.com", "pw");
        user.setName("Regular");
        user.setIsAdmin(false);
        UserInSession.create(user);

        AdminDashboardActivity activity =
                Robolectric.buildActivity(AdminDashboardActivity.class).create().get();

        assertTrue(activity.isFinishing());
    }

    @Test
    public void onCreate_withNoSession_finishes() {
        AdminDashboardActivity activity =
                Robolectric.buildActivity(AdminDashboardActivity.class).create().get();

        assertTrue(activity.isFinishing());
    }

    @Test
    public void onCreate_userMarkedAdminButNotAdminType_finishes() {
        User impostor = new User("x@test.com", "pw");
        impostor.setName("X");
        impostor.setIsAdmin(true);
        UserInSession.create(impostor);

        AdminDashboardActivity activity =
                Robolectric.buildActivity(AdminDashboardActivity.class).create().get();

        assertTrue(activity.isFinishing());
    }
}
