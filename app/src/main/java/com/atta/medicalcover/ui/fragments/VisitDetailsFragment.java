package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.atta.medicalcover.Doctor;
import com.atta.medicalcover.Prescription;
import com.atta.medicalcover.R;
import com.atta.medicalcover.ui.Appointment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VisitDetailsFragment extends Fragment {

    View root;

    TextView clinicName, dateTv, timeTv, statusTv, doctorNameTv, diagnosisTv, medicationsTv,
            labRadiologyTv, noteTv;

    Group noteGroup;

    FirebaseFirestore db;

    Doctor doctor;

    Appointment appointment;
    Prescription prescription;

    Button cancelBtn, requestServiceBtn, requestMedicationsBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_visit_details, container, false);

        appointment = VisitDetailsFragmentArgs.fromBundle(getArguments()).getAppointemnt();

        db = FirebaseFirestore.getInstance();

        initiateViews();

        return root;
    }

    private void initiateViews(){

        clinicName = root.findViewById(R.id.clinicNameTv);
        dateTv = root.findViewById(R.id.date_tv);
        timeTv = root.findViewById(R.id.time_tv);
        statusTv = root.findViewById(R.id.status_tv);
        doctorNameTv = root.findViewById(R.id.doctorNameTv);
        cancelBtn = root.findViewById(R.id.cancel_btn);
        noteTv = root.findViewById(R.id.noteText);
        noteGroup = root.findViewById(R.id.noteGroup);

        clinicName.setText(appointment.getClinicName());
        if (appointment.getStatus().equalsIgnoreCase("Finished") ||
                appointment.getStatus().equalsIgnoreCase("Canceled")){

            cancelBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setOnClickListener(null);
        }else {

            cancelBtn.setVisibility(View.VISIBLE);
            cancelBtn.setOnClickListener(view -> cancelVisit());
        }
        getDoctorDetails();

        dateTv.setText(appointment.getDate());
        String timeSlot = appointment.getTimeSlot();
        String convertedTimeSlot;
        int hours = Integer.parseInt(Arrays.asList(timeSlot.split(":")).get(0));
        String min = Arrays.asList(timeSlot.split(":")).get(1);
        if (hours <= 12 && hours > 0){
            convertedTimeSlot = timeSlot + " AM";
        }else {
            hours -= 12;
            if (hours < 10){

                convertedTimeSlot = "0" + hours + ":" + min + " PM";
            }else {

                convertedTimeSlot = hours + ":" + min + " PM";
            }
        }

        timeTv.setText(convertedTimeSlot);
        statusTv.setText(appointment.getStatus());

        switch (appointment.getStatus()){
            case "new":
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.blue));
                break;
            case "confirmed":
            case "Finished":
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.green));
                break;
            case "canceled":
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.red));
                break;
            default:
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.black));
        }

        diagnosisTv = root.findViewById(R.id.diagnosis_tv);
        medicationsTv = root.findViewById(R.id.medications_tv);
        labRadiologyTv = root.findViewById(R.id.lab_radiology_tv);


        requestServiceBtn = root.findViewById(R.id.requestServiceBtn);
        requestMedicationsBtn = root.findViewById(R.id.requestMedicationsBtn);

        getPrescription();
    }

    private void getPrescription() {
        db.collection("Prescriptions")
                .whereEqualTo("appointmentId", appointment.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        for (DocumentSnapshot document : queryDocumentSnapshots){

                            prescription = document.toObject(Prescription.class);
                            prescription.setId(document.getId());
                        }
                        diagnosisTv.setText(prescription.getDiagnosis());

                        for (int i = 0; i < prescription.getMedications().size(); i++) {
                            if (i == 0){

                                medicationsTv.setText(prescription.getMedications().get(i).toString());
                            }else {
                                medicationsTv.append("\n" + prescription.getMedications().get(i).toString());
                            }


                        }

                        for (int i = 0; i < prescription.getMedicalTests().size(); i++) {
                            if (i == 0){
                                labRadiologyTv.setText(prescription.getMedicalTests().get(i));
                            }else {
                                labRadiologyTv.append("\n" + prescription.getMedicalTests().get(i));
                            }
                        }

                        requestServiceBtn.setOnClickListener(this::requestService);
                        requestMedicationsBtn.setOnClickListener(this::requestMedications);

                        if (prescription.getNote() != null){
                            if (!prescription.getNote().equals("")){
                                noteTv.setText(prescription.getNote());
                            }else {
                                noteGroup.setVisibility(View.GONE);
                            }
                        }else {
                            noteGroup.setVisibility(View.GONE);
                        }
                    }else {
                        noteGroup.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "No prescription add to this visit", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(e -> {

                });
    }


    private void requestMedications(View view) {
        if (prescription != null && !prescription.getMedications().isEmpty()) {
            Navigation.findNavController(view)
                    .navigate(VisitDetailsFragmentDirections
                            .actionNavigationVisitDetailsToNavigationMedicationsRequest(appointment.getId(),
                                    prescription.getId()));
        }else {
            Toast.makeText(getContext(), "prescription not added yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestService(View view) {
        if (prescription != null && !prescription.getMedicalTests().isEmpty()) {
            Navigation.findNavController(view)
                    .navigate(VisitDetailsFragmentDirections
                            .actionNavigationVisitDetailsToTestsRequestFragment(prescription.getId(),
                                    appointment));
        }else {
            Toast.makeText(getContext(), "prescription not added yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelVisit() {
        if (!appointment.getStatus().equalsIgnoreCase("Finish")){
            Map<String, String> map = new HashMap<>();
            map.put("status", "Canceled");
            db.collection("Appointments").document(appointment.getId())
                    .set(map, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        appointment.setStatus("Canceled");
                        statusTv.setText(appointment.getStatus());
                        cancelBtn.setVisibility(View.INVISIBLE);
                        cancelBtn.setOnClickListener(null);
                        Toast.makeText(getContext(), "status updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error, try again", Toast.LENGTH_SHORT).show());
        }else {
            Toast.makeText(getContext(), "cannot cancel this visit", Toast.LENGTH_SHORT).show();

        }
    }

    private void getDoctorDetails(){
        db.collection("Doctors")
                .document(appointment.getDoctorId())
                .get().addOnSuccessListener(documentSnapshot -> {

                    doctor = documentSnapshot.toObject(Doctor.class);
                    doctor.setId(documentSnapshot.getId());
                    doctorNameTv.setText(doctor.getName());
                })
                .addOnFailureListener(e -> {

                });
    }
}