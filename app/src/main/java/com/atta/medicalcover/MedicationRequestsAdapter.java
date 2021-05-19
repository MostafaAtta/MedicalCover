package com.atta.medicalcover;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.RequestsFragmentDirections;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedicationRequestsAdapter extends RecyclerView.Adapter<MedicationRequestsAdapter.MyViewHolder> {

    private List<MedicationRequest> medicationRequests;
    private Activity activity;

    public MedicationRequestsAdapter(ArrayList<MedicationRequest> data, Activity activity) {

        this.medicationRequests = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medication_request_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final MedicationRequest medicationRequest = medicationRequests.get(position);

        final String patientName = medicationRequest.getPatientName();
        final String pharmacyName = medicationRequest.getPharmacyName();
        Timestamp timestamp = medicationRequest.getTimestamp();

        Date date = timestamp.toDate();

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));

        holder.patientNameTv.setText(patientName);
        holder.pharmacyNameTv.setText(pharmacyName);
        holder.dateTv.setText(format.format(date));

        String status = medicationRequest.getStatus();

        holder.statusTv.setText(status);

        switch (status){
            case "pending approval":
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.blue));
                break;
            case "Approved":
            case "Finished":
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.green));
                break;
            case "Rejected":
            case "Canceled":
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.red));
                break;
            default:
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.black));
        }




        holder.itemView.setOnClickListener(view -> {
                Navigation.findNavController(view)
                        .navigate(RequestsFragmentDirections
                        .actionRequestsFragmentToMedicationRequestDetailsFragment(medicationRequest));
        });

    }

    @Override
    public int getItemCount() {
        return medicationRequests.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView patientNameTv, dateTv, pharmacyNameTv, statusTv;

        MyViewHolder(View view) {
            super(view);
            patientNameTv = view.findViewById(R.id.patient_name_tv);
            pharmacyNameTv = view.findViewById(R.id.pharmacy_name_tv);
            dateTv = view.findViewById(R.id.date_tv);
            statusTv = view.findViewById(R.id.status_tv);

        }
    }


}
