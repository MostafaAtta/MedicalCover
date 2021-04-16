package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atta.medicalcover.R;
import com.atta.medicalcover.SpecialtiesAdapter;
import com.atta.medicalcover.Specialty;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SpecialtiesFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView recyclerView;

    SpecialtiesAdapter myAdapter;

    String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_specialties, container, false);

        type = SpecialtiesFragmentArgs.fromBundle(getArguments()).getType();

        recyclerView = root.findViewById(R.id.recyclerView);

        db = FirebaseFirestore.getInstance();

        getSpecialties();

        return root;
    }


    public void getSpecialties(){

        db.collection("Specialties")
                .orderBy("order")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){

                            ArrayList<Specialty> data = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                data.add(documentSnapshot.toObject(Specialty.class));

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

    private void add(Specialty specialty){

        db.collection("Specialties").document().set(specialty);
    }

    private void showRecycler(ArrayList<Specialty> data) {

        myAdapter = new SpecialtiesAdapter(data, getActivity(), false, type);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recyclerView.setAdapter(myAdapter);
    }
}