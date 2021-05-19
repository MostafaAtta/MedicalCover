package com.atta.medicalcover;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class MedicationRequest implements Serializable {

    private String id, appointmentId, pharmacyId, pharmacyName, patientId, patientName, prescriptionId, status;

    private Timestamp timestamp;

    public MedicationRequest(){

    }

    public MedicationRequest(String appointmentId, String pharmacyId, String pharmacyName,
                             String patientId, String patientName, String prescriptionId,
                             String status, Timestamp timestamp) {
        this.appointmentId = appointmentId;
        this.pharmacyId = pharmacyId;
        this.patientId = patientId;
        this.prescriptionId = prescriptionId;
        this.status = status;
        this.timestamp = timestamp;
        this.pharmacyName = pharmacyName;
        this.patientName = patientName;
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

    public String getPharmacyId() {
        return pharmacyId;
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

    public String getPharmacyName() {
        return pharmacyName;
    }

    public String getPatientName() {
        return patientName;
    }
}
