package com.example.popin;
import java.util.HashMap;

public class UserCatalog {
    private HashMap<String, User> users;
    private HashMap<String, Customer> customers;
    private HashMap<String, Admin> admins;

    public UserCatalog() {
        this.users = new HashMap<>();
        this.customers = new HashMap<>();
        this.admins = new HashMap<>();
    }

    public void addCustomer(String email, String password, String phoneNumber) {
        Customer customer = new Customer(email, password, phoneNumber);
        customers.put(email, customer);
        users.put(email, customer);
    }

    public void addAdmin(String email, String password, String id) {
        Admin admin = new Admin(email, password, id);
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
