package com.example.billify;

import java.io.Serializable;

class Friend implements Serializable
{
    public String name;
    public String email;
    public String contact;
    public String id;
    public long balance;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String profile;

    public Friend(String name, String email, String contact, String id, long balance) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.id = id;
        this.balance = balance;
    }

    public  Friend()
    {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
