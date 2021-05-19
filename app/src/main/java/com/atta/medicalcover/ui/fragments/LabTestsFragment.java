package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.LabTestRecord;
import com.atta.medicalcover.LabTestRecordsAdapter;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LabTestsFragment extends Fragment {

    View root;

    FloatingActionButton addLabTest;

    FirebaseFirestore db;

    RecyclerView labTestRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_lab_tests, container, false);

        initiateViews();

        return root;
    }

    private void initiateViews() {

        addLabTest = root.findViewById(R.id.addNewLabTest);

        addLabTest.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(LabTestsFragmentDirections.actionLabTestsFragmentToNewLabTestFragment()));

        labTestRecycler = root.findViewById(R.id.labTestRecycler);

        db = FirebaseFirestore.getInstance();

        getLabTests();
    }


    public void getLabTests(){
        db.collection("LabTest Records")
                .whereEqualTo("userId", SessionManager.getInstance(getContext()).getUserId())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<LabTestRecord> labTestRecords = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            LabTestRecord labTestRecord = documentSnapshot.toObject(LabTestRecord.class);
                            labTestRecord.setId(documentSnapshot.getId());
                            labTestRecords.add(labTestRecord);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(labTestRecords);
                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void showRecycler(ArrayList<LabTestRecord> labTestRecords) {
        LabTestRecordsAdapter myAdapter = new LabTestRecordsAdapter(labTestRecords, getActivity());

        labTestRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        labTestRecycler.setAdapter(myAdapter);
    }

}