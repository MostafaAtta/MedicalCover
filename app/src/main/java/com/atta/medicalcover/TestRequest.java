package com.atta.medicalcover;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class TestRequest implements Serializable {

    private String id, appointmentId, centerId, centerName, patientId, patientName, prescriptionId,
            status, type, doctorId, doctorName;

    private Timestamp timestamp;

    public TestRequest(){

    }

    public TestRequest(String appointmentId, String centerId, String centerName, String patientId,
                       String patientName, String prescriptionId, String status, Timestamp timestamp,
                       String type, String doctorId, String doctorName) {
        this.appointmentId = appointmentId;
        this.centerId = centerId;
        this.patientId = patientId;
        this.prescriptionId = prescriptionId;
        this.status = status;
        this.timestamp = timestamp;
        this.centerName = centerName;
        this.patientName = patientName;
        this.type = type;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getCenterId() {
        return centerId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getCenterName() {
        return centerName;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }


}
