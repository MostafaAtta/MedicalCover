package com.atta.medicalcover;

import java.util.ArrayList;

public class User {

    private String fullName, email, phone, city, userId, dateOfBirth, gender, membershipNumber,
            policyHolder, policyNumber, bloodType;

    private ArrayList<String> chronicDiseases;

    private ArrayList<String> tokens;

    private boolean isPregnant, isChronicDiseases;

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

    public User(String fullName, String email, String phone, String city, String dateOfBirth,
                String gender, String membershipNumber, String policyHolder, String policyNumber,
                String bloodType, ArrayList<String> chronicDiseases, boolean isPregnant,
                boolean isChronicDiseases, int type, ArrayList<String> tokens) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.membershipNumber = membershipNumber;
        this.policyHolder = policyHolder;
        this.policyNumber = policyNumber;
        this.bloodType = bloodType;
        this.chronicDiseases = chronicDiseases;
        this.isPregnant = isPregnant;
        this.isChronicDiseases = isChronicDiseases;
        this.tokens = tokens;
        this.type = type;
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

    public String getBloodType() {
        return bloodType;
    }

    public ArrayList<String> getChronicDiseases() {
        return chronicDiseases;
    }

    public boolean getIsPregnant() {
        return isPregnant;
    }

    public boolean getIsChronicDiseases() {
        return isChronicDiseases;
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }
}
