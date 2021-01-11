package com.atta.medicalcover;

import java.util.ArrayList;

public class Doctor {

    private String name, image, experience, waitingTime;
    private ArrayList<String> reviews, degrees, specialities;
    private ArrayList<Boolean> satisfied;

    public Doctor() {
    }

    public Doctor(String name, String image, String experience, String waitingTime, ArrayList<String> reviews, ArrayList<String> degrees, ArrayList<String> specialities, ArrayList<Boolean> satisfied) {
        this.name = name;
        this.image = image;
        this.experience = experience;
        this.waitingTime = waitingTime;
        this.reviews = reviews;
        this.degrees = degrees;
        this.specialities = specialities;
        this.satisfied = satisfied;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getExperience() {
        return experience;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public ArrayList<String> getDegrees() {
        return degrees;
    }

    public ArrayList<String> getSpecialities() {
        return specialities;
    }

    public ArrayList<Boolean> getSatisfied() {
        return satisfied;
    }
}
