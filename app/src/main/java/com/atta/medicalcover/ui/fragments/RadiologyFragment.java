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
import com.atta.medicalcover.RadiologyRecord;
import com.atta.medicalcover.RadiologyRecordsAdapter;
import com.atta.medicalcover.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class RadiologyFragment extends Fragment {

    View root;

    FloatingActionButton addRadiology;

    FirebaseFirestore db;

    RecyclerView radiologyRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_radiology, container, false);


        initiateViews();

        return root;
    }

    private void initiateViews() {

        addRadiology = root.findViewById(R.id.addRadiology);

        addRadiology.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(RadiologyFragmentDirections.actionRadiologyFragmentToNewRadiologyFragment()));

        radiologyRecycler = root.findViewById(R.id.radiologyRecycler);

        db = FirebaseFirestore.getInstance();

        getRadiology();
    }


    public void getRadiology(){
        db.collection("Radiology Records")
                .whereEqualTo("userId", SessionManager.getInstance(getContext()).getUserId())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<RadiologyRecord> radiologyRecords = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            RadiologyRecord labTestRecord = documentSnapshot.toObject(RadiologyRecord.class);
                            labTestRecord.setId(documentSnapshot.getId());
                            radiologyRecords.add(labTestRecord);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(radiologyRecords);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<RadiologyRecord> radiologyRecords) {
        RadiologyRecordsAdapter myAdapter = new RadiologyRecordsAdapter(radiologyRecords, getActivity());

        radiologyRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        radiologyRecycler.setAdapter(myAdapter);
    }
}