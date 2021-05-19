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

public class TestRequestsAdapter extends RecyclerView.Adapter<TestRequestsAdapter.MyViewHolder> {

    private List<TestRequest> testRequests;
    private Activity activity;

    public TestRequestsAdapter(ArrayList<TestRequest> data, Activity activity) {

        this.testRequests = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_request_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final TestRequest testRequest = testRequests.get(position);


        final String patientName = testRequest.getPatientName();
        final String centerName = testRequest.getCenterName();
        Timestamp timestamp = testRequest.getTimestamp();

        Date date = timestamp.toDate();

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));

        holder.patientNameTv.setText(patientName);
        holder.centerNameTv.setText(centerName);
        holder.dateTv.setText(format.format(date));

        String status = testRequest.getStatus();

        holder.statusTv.setText(status);

        switch (status){
            case "pending approval":
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.blue));
                break;
            case "Approved":
            case "Finished":
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.green));
                break;
            case "rejected":
            case "Canceled":
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.red));
                break;
            default:
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.black));
        }




        holder.itemView.setOnClickListener(view -> {
                Navigation.findNavController(view)
                    .navigate(RequestsFragmentDirections
                            .actionRequestsFragmentToTestRequestDetailsFragment(testRequest));
        });



    }

    @Override
    public int getItemCount() {
        return testRequests.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView patientNameTv, dateTv, centerNameTv, statusTv;

        MyViewHolder(View view) {
            super(view);
            patientNameTv = view.findViewById(R.id.patient_name_tv);
            centerNameTv = view.findViewById(R.id.center_name_tv);
            dateTv = view.findViewById(R.id.date_tv);
            statusTv = view.findViewById(R.id.status_tv);

        }
    }


}
