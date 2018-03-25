package com.firebaseauth;

/**
 * Created by Aashka on 24-03-2018.
 */

public class UserContact {
    public String name;
    public String contact;

    public UserContact(){}

    public UserContact(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
