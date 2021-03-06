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

import com.atta.medicalcover.R;
import com.atta.medicalcover.ServicesAdapter;
import com.atta.medicalcover.SessionManager;
import com.atta.medicalcover.TestCenter;
import com.atta.medicalcover.ui.Appointment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestsRequestFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView recyclerView;

    ServicesAdapter myAdapter;

    Appointment appointment;

    String prescriptionId;

    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_tests_request, container, false);

        appointment = TestsRequestFragmentArgs.fromBundle(getArguments()).getVisit();
        prescriptionId = TestsRequestFragmentArgs.fromBundle(getArguments()).getPrescriptionId();

        recyclerView = root.findViewById(R.id.lab_radiology_recyclerView);
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

        getCenters();


        return root;
    }


    private void getCenters() {

        db.collection("Services")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        ArrayList<TestCenter> data = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestCenter testCenter = documentSnapshot.toObject(TestCenter.class);
                            testCenter.setId(documentSnapshot.getId());
                            data.add(testCenter);

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

    private void getCenters(String query) {

        db.collection("Services")
                .whereIn("name", Arrays.asList(query))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        ArrayList<TestCenter> data = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestCenter testCenter = documentSnapshot.toObject(TestCenter.class);
                            testCenter.setId(documentSnapshot.getId());
                            data.add(testCenter);

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

    private void showRecycler(ArrayList<TestCenter> data) {

        myAdapter = new ServicesAdapter(data, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(myAdapter);
    }

    public void addTestRequest(TestCenter testCenter) {

        Timestamp timestamp = new Timestamp(new Date());
        Map<String, Object> request = new HashMap<>();
        request.put("appointmentId", appointment.getId());
        request.put("prescriptionId", prescriptionId);
        request.put("centerId", testCenter.getId());
        request.put("centerName", testCenter.getName());
        request.put("doctorId", appointment.getDoctorId());
        request.put("doctorName", appointment.getDoctorName());
        request.put("timestamp", timestamp);
        request.put("patientId", SessionManager.getInstance(getContext()).getUserId());
        request.put("patientName", SessionManager.getInstance(getContext()).getUsername());
        request.put("status", "pending approval");
        request.put("type", testCenter.getType());


        db.collection("Test Requests")
                .add(request)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "Request added successfully", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                                .navigate(TestsRequestFragmentDirections.actionTestsRequestFragmentToNavigationSettings());

                    }else {
                        Toast.makeText(getContext(), "something wrong, Please Try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}