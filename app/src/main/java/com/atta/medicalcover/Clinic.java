package com.atta.medicalcover;

import java.io.Serializable;
import java.util.ArrayList;

public class Clinic implements Serializable {

    private String id, name, address, doctorId, type;
    private int availableAfter;
    private double latitude, longitude;
    private ArrayList<String> afternoonSlots, eveningSlots, morningSlots;

    public Clinic() {
    }

    public Clinic(String id, String name, String address, String doctorId, String type,
                  int availableAfter, double latitude, double longitude,
                  ArrayList<String> afternoonSlots, ArrayList<String> eveningSlots,
                  ArrayList<String> morningSlots) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.doctorId = doctorId;
        this.type = type;
        this.availableAfter = availableAfter;
        this.latitude = latitude;
        this.longitude = longitude;
        this.afternoonSlots = afternoonSlots;
        this.eveningSlots = eveningSlots;
        this.morningSlots = morningSlots;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getType() {
        return type;
    }

    public int getAvailableAfter() {
        return availableAfter;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<String> getAfternoonSlots() {
        return afternoonSlots;
    }

    public ArrayList<String> getEveningSlots() {
        return eveningSlots;
    }

    public ArrayList<String> getMorningSlots() {
        return morningSlots;
    }
}
