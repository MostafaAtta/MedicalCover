package com.atta.medicalcover;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Vaccine implements Serializable {

    private String id, name;

    Timestamp timestamp;

    public Vaccine(){

    }

    public Vaccine(String name, Timestamp timestamp) {
        this.timestamp = timestamp;
        this.name = name;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
