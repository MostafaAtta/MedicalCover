package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.atta.medicalcover.MedicationRequest;
import com.atta.medicalcover.Prescription;
import com.atta.medicalcover.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MedicationRequestDetailsFragment extends Fragment {

    View root;

    TextView pharmacyName, dateTv, statusTv, diagnosisTv, medicationsTv;

    Button cancelBtn;

    MedicationRequest medicationRequest;

    Prescription prescription;

    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_medication_request_details, container, false);

        medicationRequest = MedicationRequestDetailsFragmentArgs.fromBundle(getArguments()).getRequest();

        db = FirebaseFirestore.getInstance();

        initiateViews();

        return root;
    }


    private void initiateViews(){

        medicationsTv = root.findViewById(R.id.medications_tv);
        diagnosisTv = root.findViewById(R.id.diagnosis_tv);
        pharmacyName = root.findViewById(R.id.pharmacyNameTv);
        dateTv = root.findViewById(R.id.date_tv);
        statusTv = root.findViewById(R.id.status_tv);


        cancelBtn = root.findViewById(R.id.cancel_btn);

        if (!medicationRequest.getStatus().equalsIgnoreCase("pending approval")){
            cancelBtn.setVisibility(View.GONE);
        }

        cancelBtn.setOnClickListener(v -> {
            updateStatus("Canceled");
        });



        pharmacyName.setText(medicationRequest.getPharmacyName());

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        dateTv.setText(format.format(medicationRequest.getTimestamp().toDate()));

        statusTv.setText(medicationRequest.getStatus());

        switch (medicationRequest.getStatus()){
            case "pending approval":
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.blue));
                break;
            case "Approved":
            case "Finished":
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.green));
                break;
            case "rejected":
            case "Canceled":
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.red));
                break;
            default:
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
        getPrescription();
    }


    private void getPrescription() {
        db.collection("Prescriptions")
                .document(medicationRequest.getPrescriptionId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot != null){

                        prescription = documentSnapshot.toObject(Prescription.class);
                        prescription.setId(documentSnapshot.getId());

                        diagnosisTv.setText(prescription.getDiagnosis());

                        for (int i = 0; i < prescription.getMedications().size(); i++) {
                            if (i == 0){

                                medicationsTv.setText(prescription.getMedications().get(i).toString());
                            }else {
                                medicationsTv.append("\n" + prescription.getMedications().get(i).toString());
                            }


                        }

                    }else {
                        Toast.makeText(getContext(), "No prescription add to this visit", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(e -> {

                });
    }


    private void updateStatus(String status) {

        Map<String, Object> req = new HashMap<>();
        req.put("status", status);
        db.collection("Medications Requests")
                .document(medicationRequest.getId())
                .update(req)
                .addOnSuccessListener(aVoid -> {
                    statusTv.setText(status);
                    cancelBtn.setVisibility(View.GONE);
                }).addOnFailureListener(e -> {

        });
    }
}