package com.atta.medicalcover;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class BloodGlucose implements Serializable {

    private String id, type, result;

    Timestamp timestamp;

    public BloodGlucose(){

    }

    public BloodGlucose(String type, String result, Timestamp timestamp) {
        this.type = type;
        this.result = result;
        this.timestamp = timestamp;
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

    public String getResult() {
        return result;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
