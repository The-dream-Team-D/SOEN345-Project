package com.example.popin.activityUnitTests;

import static org.junit.Assert.*;

import android.content.Intent;
import android.view.View;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.robolectric.Shadows.shadowOf;

import androidx.appcompat.app.AppCompatActivity;

import com.example.popin.FirebaseTestAssistant;
import com.example.popin.R;
import com.example.popin.uipages.AdminDashboardActivity;
import com.example.popin.uipages.EventsPageActivity;
import com.example.popin.uipages.MyTicketsActivity;
import com.example.popin.uipages.ProfileActivity;
import com.example.popin.logic.NotificationPreferenceOptions;
import com.example.popin.logic.User;
import com.example.popin.logic.UserInSession;
import com.example.popin.reusableui.NavBarComponentView;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class NavBarComponentViewTest {
    private ShadowActivity mockShadowActivity;
    private NavBarComponentView navBar;
    private AppCompatActivity mockHostActivity;

    @Before
    public void setUp() {
        setupWithAdmin(true);
    }


    @BeforeClass
    public static void setUpClass() {
        FirebaseTestAssistant.setupMockFirebase();
    }

    @AfterClass
    public static void tearDown() {
        FirebaseTestAssistant.tearDown();
    }

    private void setupWithAdmin(boolean isAdmin) {
        User u = new User("TestMan", "Test");
        u.setIsAdmin(isAdmin);
        u.setUserNotificationPreference(NotificationPreferenceOptions.EMAIL);
        UserInSession.create(u);

        mockHostActivity = Robolectric.buildActivity(AppCompatActivity.class).create().get();
        mockShadowActivity = shadowOf(mockHostActivity);
        navBar = new NavBarComponentView(mockHostActivity, null);
        NavBarComponentView.setup(navBar);
    }

    @Test
    public void exploreTab_click_launchesEventsPageActivity() {
        navBar.findViewById(R.id.nav_explore_container).performClick();

        Intent started = mockShadowActivity.getNextStartedActivity();
        assertNotNull("Expected an Intent to be started", started);
        assertEquals(EventsPageActivity.class.getName(),
                started.getComponent().getClassName());
    }

    @Test
    public void exploreTab_click_whenAlreadyOnEventsPage_doesNotLaunchNewActivity() {
        AppCompatActivity eventsActivity =
                Robolectric.buildActivity(EventsPageActivity.class).create()
                .start()
                .resume()
                .get();
        NavBarComponentView navBarOnEvents = new NavBarComponentView(eventsActivity, null);

        navBarOnEvents.findViewById(R.id.nav_explore_container).performClick();

        assertNull("Should not start a new Activity when already on EventsPageActivity",
                shadowOf(eventsActivity).getNextStartedActivity());
    }

    @Test
    public void ticketsTab_click_launchesMyTicketsActivity() {
        navBar.findViewById(R.id.nav_tickets_container).performClick();

        Intent started = mockShadowActivity.getNextStartedActivity();
        assertNotNull(started);
        assertEquals(MyTicketsActivity.class.getName(),
                started.getComponent().getClassName());
    }

    @Test
    public void ticketsTab_click_whenAlreadyOnMyTickets_doesNotLaunchNewActivity() {
        AppCompatActivity ticketsActivity =
                Robolectric.buildActivity(MyTicketsActivity.class).create().get();
        NavBarComponentView navBarOnTickets = new NavBarComponentView(ticketsActivity, null);

        navBarOnTickets.findViewById(R.id.nav_tickets_container).performClick();

        assertNull(shadowOf(ticketsActivity).getNextStartedActivity());
    }

    @Test
    public void profileTab_click_launchesProfileActivity() {
        navBar.findViewById(R.id.nav_profile_container).performClick();

        Intent started = mockShadowActivity.getNextStartedActivity();
        assertNotNull(started);
        assertEquals(ProfileActivity.class.getName(),
                started.getComponent().getClassName());
    }

    @Test
    public void profileTab_click_whenAlreadyOnProfile_doesNotLaunchNewActivity() {
        AppCompatActivity profileActivity =
                Robolectric.buildActivity(ProfileActivity.class).create().get();
        NavBarComponentView navBarOnProfile = new NavBarComponentView(profileActivity, null);

        navBarOnProfile.findViewById(R.id.nav_profile_container).performClick();

        assertNull(shadowOf(profileActivity).getNextStartedActivity());
    }

    @Test
    public void adminDashboardVisibleToAdminUser() {
        navBar.findViewById(R.id.nav_adminDashboard_container).performClick();

        Intent started = mockShadowActivity.getNextStartedActivity();
        assertNotNull(started);
        assertEquals(AdminDashboardActivity.class.getName(),
                started.getComponent().getClassName());
    }

    @Test
    public void adminDashboardNotVisibleToNonAdminUser() {
        setupWithAdmin(false);

        View adminNavBarOption = navBar.findViewById(R.id.nav_adminDashboard_container);
        assertEquals(View.GONE, adminNavBarOption.getVisibility());
    }

    
}

