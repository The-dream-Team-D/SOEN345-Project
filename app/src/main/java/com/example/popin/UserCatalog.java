package com.example.popin;

import java.util.HashMap;
import java.util.Map;

public class UserCatalog {
    private final Map<String, User> users;
    private final Map<String, Customer> customers;
    private final Map<String, Admin> admins;

    public UserCatalog() {
        this.users = new HashMap<>();
        this.customers = new HashMap<>();
        this.admins = new HashMap<>();
    }

    public void addCustomer(String name, String email, String password, String phoneNumber) {
        Customer customer = new Customer(name, email, password, phoneNumber);
        customers.put(email, customer);
        users.put(email, customer);
    }

    public void addAdmin(String name, String email, String password) {
        Admin admin = new Admin(email, password, null);
        admin.setName(name);
        admins.put(email, admin);
        users.put(email, admin);
    }

    public User getUser(String email) {
        return users.get(email);
    }

    public void removeUser(String email) {
        User user = users.get(email);
        if (user instanceof Customer) {
            customers.remove(email);
        } else if (user instanceof Admin) {
            admins.remove(email);
        }
        users.remove(email);
    }

}
