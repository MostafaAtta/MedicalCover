package com.atta.medicalcover;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.security.SecureRandom;

public class Branch implements Serializable {

    String id, name, address;

    GeoPoint location;

    public Branch(){

    }

    public Branch(String id, String name, String address, GeoPoint location) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public GeoPoint getLocation() {
        return location;
    }
}
