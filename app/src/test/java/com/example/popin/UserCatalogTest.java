package com.example.popin;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.popin.addedFiles.Admin;
import com.example.popin.addedFiles.Customer;
import com.example.popin.addedFiles.UserCatalog;

import org.junit.Test;

public class UserCatalogTest {

    @Test
    public void addCustomer_storesCustomerAsUser() {
        UserCatalog catalog = new UserCatalog();

        catalog.addCustomer("customer@example.com", "secret", "514");

        assertTrue(catalog.getUser("customer@example.com") instanceof Customer);
    }

    @Test
    public void addAdmin_storesAdminAsUser() {
        UserCatalog catalog = new UserCatalog();

        catalog.addAdmin("admin@example.com", "secret", "admin-1");

        assertTrue(catalog.getUser("admin@example.com") instanceof Admin);
    }

    @Test
    public void removeUser_removesCustomerFromCatalog() {
        UserCatalog catalog = new UserCatalog();
        catalog.addCustomer("customer@example.com", "secret", "514");

        catalog.removeUser("customer@example.com");

        assertNull(catalog.getUser("customer@example.com"));
    }

    @Test
    public void removeUser_removesAdminFromCatalog() {
        UserCatalog catalog = new UserCatalog();
        catalog.addAdmin("admin@example.com", "secret", "admin-1");

        catalog.removeUser("admin@example.com");

        assertNull(catalog.getUser("admin@example.com"));
    }

    @Test
    public void removeUser_missingEmail_keepsCatalogStable() {
        UserCatalog catalog = new UserCatalog();
        catalog.addCustomer("customer@example.com", "secret", "514");

        catalog.removeUser("missing@example.com");

        assertTrue(catalog.getUser("customer@example.com") instanceof Customer);
    }
}
