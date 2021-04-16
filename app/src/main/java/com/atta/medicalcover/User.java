package com.atta.medicalcover;

public class User {

    private String fullName, email, phone, city, userId, dateOfBirth, gender, membershipNumber,
            policyHolder, policyNumber;

    private int type;

    public User() {
    }

    public User(String fullName, String email, String phone, String city, String membershipNumber,
                String policyHolder, String policyNumber) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.userId = userId;
        this.membershipNumber = membershipNumber;
        this.policyHolder = policyHolder;
        this.policyNumber = policyNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public String getPolicyHolder() {
        return policyHolder;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public int getType() {
        return type;
    }
}
