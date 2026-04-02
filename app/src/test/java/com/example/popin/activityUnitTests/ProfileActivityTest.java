package com.example.popin.activityUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.popin.R;
import com.example.popin.UIpages.ProfileActivity;
import com.example.popin.logic.User;
import com.example.popin.logic.UserInSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class ProfileActivityTest {

    private ProfileActivity activity;
    private User testUser;

    @Before
    public void setUp() {
        testUser = new User("hos@email.com", "123456");
        testUser.setName("HOSSAM THE GOAT");
        testUser.setAddress("123 concordia");
        testUser.setPhoneNumber("123456789");
        testUser.setBio("bio 123");
        testUser.setIsAdmin(true);

        UserInSession.create(testUser);

        activity = Robolectric.buildActivity(ProfileActivity.class)
                .create()
                .start()
                .resume()
                .get();
    }

    @After
    public void tearDown() {
        UserInSession.clear();
    }

    @Test
    public void activityShouldLaunch() {
        assertNotNull(activity);
    }

    @Test
    public void viewsShouldExist() {
        TextView tvName = activity.findViewById(R.id.tvName);
        TextView tvEmail = activity.findViewById(R.id.tvEmail);
        TextView tvAddress = activity.findViewById(R.id.tvAddress);
        TextView tvPhone = activity.findViewById(R.id.tvPhone);
        TextView tvBio = activity.findViewById(R.id.tvBio);
        TextView tvRole = activity.findViewById(R.id.tvRole);

        EditText etName = activity.findViewById(R.id.etName);
        EditText etAddress = activity.findViewById(R.id.etAddress);
        EditText etPhone = activity.findViewById(R.id.etPhone);
        EditText etBio = activity.findViewById(R.id.etBio);

        Button btnEdit = activity.findViewById(R.id.btnEdit);
        Button btnSave = activity.findViewById(R.id.btnSave);

        assertNotNull(tvName);
        assertNotNull(tvEmail);
        assertNotNull(tvAddress);
        assertNotNull(tvPhone);
        assertNotNull(tvBio);
        assertNotNull(tvRole);
        assertNotNull(etName);
        assertNotNull(etAddress);
        assertNotNull(etPhone);
        assertNotNull(etBio);
        assertNotNull(btnEdit);
        assertNotNull(btnSave);
    }

    @Test
    public void userDataShouldDisplayCorrectly() {
        TextView tvName = activity.findViewById(R.id.tvName);
        TextView tvEmail = activity.findViewById(R.id.tvEmail);
        TextView tvAddress = activity.findViewById(R.id.tvAddress);
        TextView tvPhone = activity.findViewById(R.id.tvPhone);
        TextView tvBio = activity.findViewById(R.id.tvBio);
        TextView tvRole = activity.findViewById(R.id.tvRole);

        assertEquals("Name: HOSSAM THE GOAT", tvName.getText().toString());
        assertEquals("Email: hos@email.com", tvEmail.getText().toString());
        assertEquals("Address: 123 concordia", tvAddress.getText().toString());
        assertEquals("Phone: 123456789", tvPhone.getText().toString());
        assertEquals("Bio: bio 123", tvBio.getText().toString());
        assertEquals("Account Type: Admin", tvRole.getText().toString());
    }

    @Test
    public void clickingEditShouldShowEditMode() {
        Button btnEdit = activity.findViewById(R.id.btnEdit);
        Button btnSave = activity.findViewById(R.id.btnSave);
        EditText etName = activity.findViewById(R.id.etName);
        EditText etAddress = activity.findViewById(R.id.etAddress);
        EditText etPhone = activity.findViewById(R.id.etPhone);
        EditText etBio = activity.findViewById(R.id.etBio);

        btnEdit.performClick();

        assertEquals(EditText.VISIBLE, etName.getVisibility());
        assertEquals(EditText.VISIBLE, etAddress.getVisibility());
        assertEquals(EditText.VISIBLE, etPhone.getVisibility());
        assertEquals(EditText.VISIBLE, etBio.getVisibility());
        assertEquals(Button.VISIBLE, btnSave.getVisibility());
        assertEquals(Button.GONE, btnEdit.getVisibility());
    }

    @Test
    public void editFieldsShouldBePrefilled() {
        Button btnEdit = activity.findViewById(R.id.btnEdit);
        EditText etName = activity.findViewById(R.id.etName);
        EditText etAddress = activity.findViewById(R.id.etAddress);
        EditText etPhone = activity.findViewById(R.id.etPhone);
        EditText etBio = activity.findViewById(R.id.etBio);

        btnEdit.performClick();

        assertEquals("HOSSAM THE GOAT", etName.getText().toString());
        assertEquals("123 concordia", etAddress.getText().toString());
        assertEquals("123456789", etPhone.getText().toString());
        assertEquals("bio 123", etBio.getText().toString());
    }
}