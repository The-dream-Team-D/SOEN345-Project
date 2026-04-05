package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.popin.addedFiles.User;

import org.junit.Test;

public class UserModelTest {

    @Test
    public void constructor_initializesDefaultFields() {
        User user = new User("user@example.com", "secret");

        assertEquals("user@example.com", user.getEmail());
        assertEquals("secret", user.getPassword());
        assertEquals("", user.getName());
        assertEquals("", user.getAddress());
        assertEquals("", user.getPhone());
        assertEquals("", user.getBio());
        assertFalse(user.getIsAdmin());
    }

    @Test
    public void emptyConstructor_keepsNullCredentials() {
        User user = new User();

        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }

    @Test
    public void setters_updateMutableFields() {
        User user = new User("user@example.com", "secret");

        user.setName("Kevin");
        user.setAddress("123 Main");
        user.setPhone("+15145551234");
        user.setBio("Bio");
        user.setPassword("new-secret");
        user.setIsAdmin(true);

        assertEquals("Kevin", user.getName());
        assertEquals("123 Main", user.getAddress());
        assertEquals("+15145551234", user.getPhone());
        assertEquals("Bio", user.getBio());
        assertEquals("new-secret", user.getPassword());
        assertTrue(user.getIsAdmin());
    }

    @Test
    public void login_withEmptyEmail_returnsValidationError() {
        User user = new User("", "secret");
        user.login(new User.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                throw new AssertionError("Expected onError for empty email");
            }

            @Override
            public void onError(String message) {
                assertEquals("Email/Phone input is Empty", message);
            }
        });
    }

    @Test
    public void login_withEmptyPassword_returnsValidationError() {
        User user = new User("user@example.com", "  ");
        user.login(new User.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                throw new AssertionError("Expected onError for empty password");
            }

            @Override
            public void onError(String message) {
                assertEquals("Password input is Empty", message);
            }
        });
    }
}

