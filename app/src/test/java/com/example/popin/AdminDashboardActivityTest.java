package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.view.View;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class AdminDashboardActivityTest {

    @After
    public void tearDown() {
        UserInSession.clear();
    }

    @Test
    public void activity_launchesAndShowsTitleForAdmin() {
        User admin = new User("admin@email.com", "pass");
        admin.setIsAdmin(true);
        UserInSession.create(admin);

        AdminDashboardActivity activity = Robolectric.buildActivity(AdminDashboardActivity.class)
                .setup()
                .get();

        assertNotNull(activity.findViewById(R.id.AdminDashboardTitle));
        View adminTab = activity.findViewById(R.id.nav_adminDashboard_container);
        assertEquals(View.VISIBLE, adminTab.getVisibility());
    }

    @Test
    public void activity_hidesAdminTabForNonAdminUser() {
        User regular = new User("user@email.com", "pass");
        regular.setIsAdmin(false);
        UserInSession.create(regular);

        AdminDashboardActivity activity = Robolectric.buildActivity(AdminDashboardActivity.class)
                .setup()
                .get();

        View adminTab = activity.findViewById(R.id.nav_adminDashboard_container);
        assertEquals(View.GONE, adminTab.getVisibility());
    }
}
