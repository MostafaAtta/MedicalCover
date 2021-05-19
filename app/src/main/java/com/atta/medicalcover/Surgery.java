package com.atta.medicalcover;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Surgery implements Serializable {


    String id, title, note, doctorId, doctorName, placeName, userId;
    Timestamp timestamp;

    public Surgery() {

    }

    public Surgery(String title, String note, String doctorId, String doctorName, String placeName,
                   String userId, Timestamp timestamp) {
        this.title = title;
        this.note = note;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.placeName = placeName;
        this.userId = userId;
        this.timestamp = timestamp;
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

    public String getNote() {
        return note;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getUserId() {
        return userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
