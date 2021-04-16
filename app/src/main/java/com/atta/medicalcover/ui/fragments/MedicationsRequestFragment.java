package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.PharmaciesAdapter;
import com.atta.medicalcover.Pharmacy;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MedicationsRequestFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView recyclerView;

    PharmaciesAdapter myAdapter;

    String visitId, prescriptionId;

    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_medications_request, container, false);

        visitId = MedicationsRequestFragmentArgs.fromBundle(getArguments()).getVisitId();
        prescriptionId = MedicationsRequestFragmentArgs.fromBundle(getArguments()).getPrescriptionId();

        recyclerView = root.findViewById(R.id.pharmacies_recyclerView);
        searchView = root.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //myAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return false;
            }
        });
        db = FirebaseFirestore.getInstance();

        getPharmacies();

        return  root;
    }

    private void getPharmacies() {

        db.collection("Pharmacies")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        ArrayList<Pharmacy> data = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Pharmacy pharmacy = documentSnapshot.toObject(Pharmacy.class);
                            pharmacy.setId(documentSnapshot.getId());
                            data.add(pharmacy);

                            //add(documentSnapshot.toObject(Specialty.class));
                        }

                        showRecycler(data);
                    }else {
                        Toast.makeText(getContext(), "something wrong, Please Try again later", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getPharmacies(String query) {

        db.collection("Pharmacies")
                .whereIn("name", Arrays.asList(query))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        ArrayList<Pharmacy> data = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Pharmacy pharmacy = documentSnapshot.toObject(Pharmacy.class);
                            pharmacy.setId(documentSnapshot.getId());
                            data.add(pharmacy);

                            //add(documentSnapshot.toObject(Specialty.class));
                        }

                        showRecycler(data);
                    }else {
                        Toast.makeText(getContext(), "something wrong, Please Try again later", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRecycler(ArrayList<Pharmacy> data) {

        myAdapter = new PharmaciesAdapter(data, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(myAdapter);
    }

    public void addMedicationsRequest(Pharmacy pharmacy){
        Map<String, Object> request = new HashMap<>();
        request.put("appointmentId", visitId);
        request.put("prescriptionId", prescriptionId);
        request.put("patientId", SessionManager.getInstance(getContext()).getUserId());
        request.put("status", "pending approval");


        db.collection("Medications Requests")
                .add(request)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "Request added successfully", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                                .navigate(MedicationsRequestFragmentDirections.actionNavigationMedicationsRequestToNavigationSettings());

                    }else {
                        Toast.makeText(getContext(), "something wrong, Please Try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}