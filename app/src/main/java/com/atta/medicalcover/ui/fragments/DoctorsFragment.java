package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.Clinic;
import com.atta.medicalcover.ClinicsAdapter;
import com.atta.medicalcover.Doctor;
import com.atta.medicalcover.DoctorsAdapter;
import com.atta.medicalcover.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DoctorsFragment extends Fragment {

    View root;

    String specialty, type;

    FirebaseFirestore db;

    RecyclerView recyclerView, clinicsRecyclerView;

    TextView sheetText;

    DoctorsAdapter myAdapter;

    ClinicsAdapter clinicsAAdapter;

    ConstraintLayout constraintLayout;
    BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_doctors, container, false);

        specialty = DoctorsFragmentArgs.fromBundle(getArguments()).getSpecialty();
        type = DoctorsFragmentArgs.fromBundle(getArguments()).getClinicType();

        recyclerView = root.findViewById(R.id.doctors_recyclerView);

        constraintLayout = root.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);

        sheetText = root.findViewById(R.id.bottom_tv);
        clinicsRecyclerView = root.findViewById(R.id.clinics_list);

        db = FirebaseFirestore.getInstance();
        if (type == null){
            getDoctors(Arrays.asList("Internal", "External", "Internal and External"));

        }else {
            getDoctors(Arrays.asList(type, "Internal and External"));
        }

        return root;
    }

    public void openSheet(Doctor doctor){

        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED ) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            sheetText.setText("Book appointment with " + doctor.getName() + " at");
            if ((type == null)) {
                getClinics(Arrays.asList("Internal", "External"), doctor);
            } else {
                getClinics(Collections.singletonList(type), doctor);
            }

        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    public void getDoctors(List<String> types){

        db.collection("Doctors")
                .whereArrayContains("specialities", specialty)
                .whereIn("type", types)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){

                            ArrayList<Doctor> data = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                Doctor doctor = documentSnapshot.toObject(Doctor.class);
                                doctor.setId(documentSnapshot.getId());
                                data.add(doctor);

                                //add(documentSnapshot.toObject(Doctor.class));
                            }

                            showRecycler(data);
                        }else {
                            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getClinics(List<String> types, Doctor doctor){

        db.collection("Clinics")
                .whereEqualTo("doctorId", doctor.getId())
                .whereIn("type", types)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){

                            ArrayList<Clinic> data = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                Clinic clinic = documentSnapshot.toObject(Clinic.class);
                                clinic.setId(documentSnapshot.getId());
                                data.add(clinic);

                                //addClinic(documentSnapshot.toObject(Clinic.class));
                            }

                            showClinicsRecycler(data, doctor);
                        }else {
                            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRecycler(ArrayList<Doctor> data) {

        myAdapter = new DoctorsAdapter(data, getActivity(), this);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(myAdapter);

    }


    private void showClinicsRecycler(ArrayList<Clinic> data, Doctor doctor) {

        clinicsAAdapter = new ClinicsAdapter(data, true, doctor);

        clinicsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        clinicsRecyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        clinicsRecyclerView.setAdapter(clinicsAAdapter);
    }


    private void add(Doctor doctor){

        db.collection("Doctors").document().set(doctor);
    }


    private void addClinic(Clinic clinic){

        db.collection("Clinics").document().set(clinic);
    }

}