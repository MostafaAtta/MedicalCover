package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atta.medicalcover.Doctor;
import com.atta.medicalcover.DoctorsAdapter;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SpecialtiesAdapter;
import com.atta.medicalcover.Specialty;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class DoctorsFragment extends Fragment {

    View root;

    String specialty, type;

    FirebaseFirestore db;

    RecyclerView recyclerView;

    DoctorsAdapter myAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_doctors, container, false);

        specialty = DoctorsFragmentArgs.fromBundle(getArguments()).getSpecialty();
        type = DoctorsFragmentArgs.fromBundle(getArguments()).getClinicType();

        recyclerView = root.findViewById(R.id.doctors_recyclerView);

        db = FirebaseFirestore.getInstance();
        if (type == null){

            getDoctors();
        }else {
            getDoctorsByType();
        }

        return root;
    }


    public void getDoctors(){

        db.collection("Doctors")
                .whereArrayContains("specialities", specialty)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){

                            ArrayList<Doctor> data = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                data.add(documentSnapshot.toObject(Doctor.class));

                                //add(documentSnapshot.toObject(Specialty.class));
                            }

                            showRecycler(data);
                        }else {
                            Toast.makeText(getContext(), "User not found, Please Register", Toast.LENGTH_SHORT).show();
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

    public void getDoctorsByType(){

        db.collection("Doctors")
                .whereArrayContains("specialities", specialty)
                .whereIn("type", Arrays.asList(type, "internal and external"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){

                            ArrayList<Doctor> data = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                data.add(documentSnapshot.toObject(Doctor.class));

                                //add(documentSnapshot.toObject(Specialty.class));
                            }

                            showRecycler(data);
                        }else {
                            Toast.makeText(getContext(), "User not found, Please Register", Toast.LENGTH_SHORT).show();
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

        myAdapter = new DoctorsAdapter(data, getActivity());

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(myAdapter);
    }
}