package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atta.medicalcover.Doctor;
import com.atta.medicalcover.Pharmacy;
import com.atta.medicalcover.Prescription;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.atta.medicalcover.TestCenter;
import com.atta.medicalcover.ui.Appointment;
import com.atta.medicalcover.ui.AppointmentsAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VisitsFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    private static final String TAG = "VisitsFragment";

    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;

    Pharmacy pharmacy;

    TestCenter testCenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_visits, container, false);

        recyclerView = root.findViewById(R.id.appointments);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);

        pharmacy = VisitsFragmentArgs.fromBundle(getArguments()).getPharmacy();
        testCenter = VisitsFragmentArgs.fromBundle(getArguments()).getTestCenter();

        db = FirebaseFirestore.getInstance();
        getAppointment();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAppointment();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return root;
    }

    public void getAppointment(){
        db.collection("Appointments")
                .whereEqualTo("userId", SessionManager.getInstance(getContext()).getUserId())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .orderBy("timeSlot", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<Appointment> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Log.d(TAG, "Cached document data: " + documentSnapshot.getData());
                            Appointment appointment = documentSnapshot.toObject(Appointment.class);
                            appointment.setId(documentSnapshot.getId());
                            appointments.add(appointment);
                            //updateDoctorName(appointment);
                        }

                        showSlotsRecycler(appointments);
                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "Cached document data: " + e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateDoctorName(Appointment appointment) {
        db.collection("Doctors")
                .document(appointment.getDoctorId())
                .get().addOnSuccessListener(documentSnapshot1 -> {

                    Doctor doctor = documentSnapshot1.toObject(Doctor.class);
                    db.collection("Appointments")
                            .document(appointment.getId())
                            .update("doctorName", doctor.getName());
                });
    }

    private void showSlotsRecycler(ArrayList<Appointment> appointments) {
        AppointmentsAdapter myAdapter = new AppointmentsAdapter(appointments,
                getActivity(), this, pharmacy != null,
                testCenter != null);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(myAdapter);
    }


    public void checkForMedicationsRequest(Appointment appointment){
        getPrescription(appointment);

    }

    public void checkForTestRequest(Appointment appointment){
        getPrescription(appointment);

    }


    private void getPrescription(Appointment appointment) {
        db.collection("Prescriptions")
                .whereEqualTo("appointmentId", appointment.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Prescription prescription = null;

                    if (!queryDocumentSnapshots.isEmpty()){
                        for (DocumentSnapshot document : queryDocumentSnapshots){

                            prescription = document.toObject(Prescription.class);
                            prescription.setId(document.getId());
                        }

                        if (pharmacy != null){
                            addMedicationsRequest(prescription, appointment);
                        }else if (testCenter != null){

                            addTestRequest(prescription, appointment);
                        }


                    }else {
                        Toast.makeText(getContext(), "No prescription add to this visit", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(e -> {

                });
    }

    public void addMedicationsRequest(Prescription prescription, Appointment appointment){

        if (prescription != null){
            if (!prescription.getMedications().isEmpty()){
                Timestamp timestamp = new Timestamp(new Date());

                Map<String, Object> request = new HashMap<>();
                request.put("appointmentId", appointment.getId());
                request.put("prescriptionId", prescription.getId());
                request.put("pharmacyId", pharmacy.getId());
                request.put("timestamp", timestamp);
                request.put("patientId", SessionManager.getInstance(getContext()).getUserId());
                request.put("status", "pending approval");


                db.collection("Medications Requests")
                        .add(request)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Request added successfully", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                                        .navigate(VisitsFragmentDirections.actionNavigationVisitsToNavigationHome());

                            }else {
                                Toast.makeText(getContext(), "something wrong, Please Try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else{
                Toast.makeText(getContext(), "No Medications add for this visit", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "No Medications add for this visit", Toast.LENGTH_SHORT).show();
        }

    }


    public void addTestRequest(Prescription prescription, Appointment appointment) {
        if (prescription != null) {
            if (!prescription.getMedications().isEmpty()) {

                Timestamp timestamp = new Timestamp(new Date());
                Map<String, Object> request = new HashMap<>();
                request.put("appointmentId", appointment.getId());
                request.put("prescriptionId", prescription.getId());
                request.put("centerId", testCenter.getId());
                request.put("timestamp", timestamp);
                request.put("type", testCenter.getType());
                request.put("patientId", SessionManager.getInstance(getContext()).getUserId());
                request.put("status", "pending approval");


                db.collection("Test Requests")
                        .add(request)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Request added successfully", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                                        .navigate(VisitsFragmentDirections.actionNavigationVisitsToNavigationHome());

                            }else {
                                Toast.makeText(getContext(), "something wrong, Please Try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else{
                Toast.makeText(getContext(), "No Medications add for this visit", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "No Medications add for this visit", Toast.LENGTH_SHORT).show();
        }

    }
}