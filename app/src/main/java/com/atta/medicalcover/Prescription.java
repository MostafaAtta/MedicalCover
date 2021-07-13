package com.atta.medicalcover;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Prescription {

    String id, appointmentId, patientName, patientId, policyNo, membershipNo, policyHolder,
            diagnosis, note;

    Timestamp creationDate;

    ArrayList<String> medicalTests;

    ArrayList<Medication> medications;

    public Prescription() {

    }

    public Prescription(String patientName, String patientId, String policyNo, String membershipNo,
                        String policyHolder, String diagnosis, Timestamp creationDate,
                        ArrayList<String> medicalTests, ArrayList<Medication> medications) {
        this.patientName = patientName;
        this.patientId = patientId;
        this.policyNo = policyNo;
        this.membershipNo = membershipNo;
        this.policyHolder = policyHolder;
        this.diagnosis = diagnosis;
        this.creationDate = creationDate;
        this.medicalTests = medicalTests;
        this.medications = medications;
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

    public String getPatientName() {
        return patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public String getMembershipNo() {
        return membershipNo;
    }

    public String getPolicyHolder() {
        return policyHolder;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public ArrayList<String> getMedicalTests() {
        return medicalTests;
    }

    public ArrayList<Medication> getMedications() {
        return medications;
    }

    public String getNote() {
        return note;
    }
}
