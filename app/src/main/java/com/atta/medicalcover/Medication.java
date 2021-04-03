package com.atta.medicalcover;

public class Medication {

    String name, dose, duration;

    public Medication() {
    }

    public Medication(String name, String dose, String duration) {
        this.name = name;
        this.dose = dose;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDose() {
        return dose;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return  name +
                ", dose='" + dose + '\'' +
                ", duration='" + duration + '\'' ;
    }
}
