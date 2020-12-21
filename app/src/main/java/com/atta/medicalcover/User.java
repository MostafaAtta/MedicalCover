package com.atta.medicalcover;

public class User {

    String fullName, email, phone, city;

    public User() {
    }

    public User(String fullName, String email, String phone, String city) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.city = city;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }
}
