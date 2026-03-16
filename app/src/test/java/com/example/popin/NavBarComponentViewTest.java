package com.example.popin;

import static org.junit.Assert.*;

import android.content.Intent;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.robolectric.Shadows.shadowOf;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class NavBarComponentViewTest {
    private ShadowActivity mockShadowActivity;
    private NavBarComponentView navBar;
    private static MockedStatic<FirebaseDatabase> mockedFirebaseDatabase;

    private AppCompatActivity mockHostActivity;

    @BeforeClass
    public static void setUpClass() {
        FirebaseTestAssistant.setupMockFirebase();
        mockedFirebaseDatabase = FirebaseTestAssistant.getMockedDatabase(); // expose the static ref
    }

    @AfterClass
    public static void tearDown() {
        FirebaseTestAssistant.tearDown();
    }

    @Before
    public void setUp() {
        mockHostActivity = Robolectric.buildActivity(AppCompatActivity.class).create().get();
        mockShadowActivity = shadowOf(mockHostActivity);
        navBar = new NavBarComponentView(mockHostActivity, null);
    }

// ── Explore tab ──────────────────────────────────────────────────────────

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
                Robolectric.buildActivity(EventsPageActivity.class).create().get();
        NavBarComponentView navBarOnEvents = new NavBarComponentView(eventsActivity, null);

        navBarOnEvents.findViewById(R.id.nav_explore_container).performClick();

        assertNull("Should not start a new Activity when already on EventsPageActivity",
                shadowOf(eventsActivity).getNextStartedActivity());
    }

// ── Tickets tab ──────────────────────────────────────────────────────────

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

// ── Profile tab ──────────────────────────────────────────────────────────

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
    
}
