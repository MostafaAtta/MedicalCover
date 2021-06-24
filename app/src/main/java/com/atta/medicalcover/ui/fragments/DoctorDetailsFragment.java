package com.atta.medicalcover.ui.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.Clinic;
import com.atta.medicalcover.ClinicsAdapter;
import com.atta.medicalcover.Doctor;
import com.atta.medicalcover.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DoctorDetailsFragment extends Fragment {

    View root;

    Doctor doctor;

    ClinicsAdapter clinicsAAdapter;

    FirebaseFirestore db;

    RecyclerView clinicsRecyclerView;

    TextView doctorName, specialities, degrees, waitingTime, experience, satisfied, reviews;

    ImageView doctorImage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_doctor_details, container, false);

        doctor = DoctorDetailsFragmentArgs.fromBundle(getArguments()).getDoctor();

        db = FirebaseFirestore.getInstance();

        initiateViews();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initiateViews(){

        clinicsRecyclerView = root.findViewById(R.id.clinics_recyclerView);
        doctorName = root.findViewById(R.id.doctor_name_tv);
        doctorImage = root.findViewById(R.id.doctor_image);
        specialities = root.findViewById(R.id.clinic_tv);
        degrees = root.findViewById(R.id.degrees_tv);
        waitingTime = root.findViewById(R.id.wait_time_tv);
        experience = root.findViewById(R.id.experience_tv);
        satisfied = root.findViewById(R.id.satisfied_tv);

        int sizeOfSatisfied = doctor.getSatisfied().size();
        int noOfSatisfied = 0;
        for (boolean s: doctor.getSatisfied()) {
            if (s){
                noOfSatisfied++;
            }
        }

        int satisfactionPercentage = (int) ( ( (float) noOfSatisfied/sizeOfSatisfied)*100 );

        String satisfactionPercent = satisfactionPercentage + "% (" + sizeOfSatisfied + ")";

        doctorName.setText(doctor.getName());
        experience.setText(doctor.getExperience());
        waitingTime.setText(doctor.getWaitingTime());
        specialities.setText(String.join(", ", doctor.getSpecialities()));
        degrees.setText(String.join(", ", doctor.getDegrees()));
        satisfied.setText(satisfactionPercent);


        if (doctor.getImage() != null) {

            Picasso.get()
                    .load(doctor.getImage())
                    .resize(80, 80)
                    .centerCrop()
                    .into(doctorImage);


        }

        getClinics();
    }


    public void getClinics(){

        db.collection("Clinics")
                .whereEqualTo("doctorId", doctor.getId())
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

                            showClinicsRecycler(data);
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


    private void showClinicsRecycler(ArrayList<Clinic> data) {

        clinicsAAdapter = new ClinicsAdapter(data, false, doctor);

        clinicsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        clinicsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        clinicsRecyclerView.setAdapter(clinicsAAdapter);
    }

}