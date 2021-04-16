package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.atta.medicalcover.ui.Appointment;
import com.atta.medicalcover.ui.AppointmentsAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VisitsFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    private static final String TAG = "VisitsFragment";

    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_visits, container, false);

        recyclerView = root.findViewById(R.id.appointments);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);

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
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){
                            ArrayList<Appointment> appointments = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                Log.d(TAG, "Cached document data: " + documentSnapshot.getData());
                                Appointment appointment = documentSnapshot.toObject(Appointment.class);
                                appointment.setId(documentSnapshot.getId());
                                appointments.add(appointment);
                            }

                            showSlotsRecycler(appointments);
                        }


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

    private void showSlotsRecycler(ArrayList<Appointment> appointments) {
        AppointmentsAdapter myAdapter = new AppointmentsAdapter(appointments, getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(myAdapter);
    }
}