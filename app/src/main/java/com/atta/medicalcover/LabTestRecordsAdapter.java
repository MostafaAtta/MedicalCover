package com.atta.medicalcover;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.LabTestsFragmentDirections;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LabTestRecordsAdapter extends RecyclerView.Adapter<LabTestRecordsAdapter.MyViewHolder> {

    private final List<LabTestRecord> labTestRecords;
    private final Activity activity;


    public LabTestRecordsAdapter(ArrayList<LabTestRecord> data, Activity activity) {

        this.labTestRecords = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lab_test_record_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final LabTestRecord labTestRecord = labTestRecords.get(position) ;


        Timestamp timestamp = labTestRecord.getTimestamp();
        Date date = timestamp.toDate();

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        holder.date.setText("Results for: " + format.format(date));




        holder.itemView.setOnClickListener(v ->

                Navigation.findNavController(v)
                        .navigate(LabTestsFragmentDirections.actionLabTestsFragmentToLabTestDetailsFragment(labTestRecord))
        );



    }

    @Override
    public int getItemCount() {
        return labTestRecords.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date;


        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.lab_test_title);

        }
    }


}
