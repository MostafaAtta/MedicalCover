package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.atta.medicalcover.Surgery;
import com.atta.medicalcover.SurgeryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SurgeryFragment extends Fragment {

    View root;

    FloatingActionButton addSurgery;

    FirebaseFirestore db;

    RecyclerView surgeryRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_surgery, container, false);

        initiateViews();

        return root;
    }


    private void initiateViews() {

        addSurgery = root.findViewById(R.id.addSurgery);

        addSurgery.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SurgeryFragmentDirections.actionSurgeryFragmentToNewSurgeryFragment()));

        surgeryRecycler = root.findViewById(R.id.surgeryRecycler);

        db = FirebaseFirestore.getInstance();

        getSurgeries();
    }


    public void getSurgeries(){
        db.collection("Surgery")
                .whereEqualTo("userId", SessionManager.getInstance(getContext()).getUserId())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<Surgery> surgeries = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Surgery surgery = documentSnapshot.toObject(Surgery.class);
                            surgery.setId(documentSnapshot.getId());
                            surgeries.add(surgery);
                            //updateDoctorName(surgery);
                        }

                        showRecycler(surgeries);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<Surgery> surgeries) {
        SurgeryAdapter myAdapter = new SurgeryAdapter(surgeries, getActivity());

        surgeryRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        surgeryRecycler.setAdapter(myAdapter);
    }
}