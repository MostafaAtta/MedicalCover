package com.atta.medicalcover;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class BloodPressure implements Serializable {

    private String id, systolic, diastolic, pulse;

    Timestamp timestamp;

    public BloodPressure(){

    }

    public BloodPressure(String systolic, String diastolic, String pulse, Timestamp timestamp) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulse = pulse;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystolic() {
        return systolic;
    }

    public String getDiastolic() {
        return diastolic;
    }

    public String getPulse() {
        return pulse;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
