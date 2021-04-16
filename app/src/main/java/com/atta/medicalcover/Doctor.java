package com.atta.medicalcover;

import java.io.Serializable;
import java.util.ArrayList;

public class Doctor implements Serializable {

    private String id, name, image, experience, waitingTime, type;
    private ArrayList<String> reviews, degrees, specialities;
    private ArrayList<Boolean> satisfied;

    public Doctor(){
    }

    public Doctor(String id, String name, String image, String experience, String waitingTime,
                  String type, ArrayList<String> reviews, ArrayList<String> degrees,
                  ArrayList<String> specialities, ArrayList<Boolean> satisfied) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.experience = experience;
        this.waitingTime = waitingTime;
        this.type = type;
        this.reviews = reviews;
        this.degrees = degrees;
        this.specialities = specialities;
        this.satisfied = satisfied;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
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
