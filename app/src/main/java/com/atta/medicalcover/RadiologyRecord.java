package com.atta.medicalcover;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class RadiologyRecord implements Serializable {


    String id, title, result, doctorId, centerId, doctorName, centerName, userId;
    Timestamp timestamp;
    ArrayList<String> images;

    public RadiologyRecord() {

    }

    public RadiologyRecord(String title, String result, String doctorId, String centerId, String doctorName,
                           String centerName, String userId, Timestamp timestamp, ArrayList<String> images) {
        this.title = title;
        this.doctorId = doctorId;
        this.centerId = centerId;
        this.doctorName = doctorName;
        this.centerName = centerName;
        this.userId = userId;
        this.timestamp = timestamp;
        this.images = images;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getCenterId() {
        return centerId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getCenterName() {
        return centerName;
    }

    public String getUserId() {
        return userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public ArrayList<String> getImages() {
        return images;
    }
}
