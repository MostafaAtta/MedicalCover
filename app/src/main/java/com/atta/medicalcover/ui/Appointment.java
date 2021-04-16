package com.atta.medicalcover.ui;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Appointment implements Serializable {

    private String id, clinicName, clinicId, date, timeSlot, doctorId, status, userId;

    private Timestamp timestamp;

    public Appointment() {
    }

    public Appointment(String clinicName, String clinicId, String date, String timeSlot, String doctorId, String status, String userId) {
        this.clinicName = clinicName;
        this.clinicId = clinicId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.doctorId = doctorId;
        this.status = status;
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getClinicId() {
        return clinicId;
    }

    public String getDate() {
        return date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getDoctorId() {
        return doctorId;
    }
}
