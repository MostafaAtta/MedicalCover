package com.atta.medicalcover;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class LabTestRecord implements Serializable {

    ArrayList<LabTest> labTests;
    String id, note, doctorId, labId, doctorName, labName, userId;
    Timestamp timestamp;
    ArrayList<String> images;

    public LabTestRecord() {

    }

    public LabTestRecord(String userId, ArrayList<LabTest> labTests, String note, String doctorId, String labId,
                         String doctorName, String labName, Timestamp timestamp,
                         ArrayList<String> images) {
        this.labTests = labTests;
        this.note = note;
        this.doctorId = doctorId;
        this.labId = labId;
        this.doctorName = doctorName;
        this.labName = labName;
        this.timestamp = timestamp;
        this.images = images;
        this.userId = userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<LabTest> getLabTests() {
        return labTests;
    }

    public String getNote() {
        return note;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getLabId() {
        return labId;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getLabName() {
        return labName;
    }


}
