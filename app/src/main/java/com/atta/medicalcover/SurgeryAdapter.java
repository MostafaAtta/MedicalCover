package com.atta.medicalcover;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SurgeryAdapter extends RecyclerView.Adapter<SurgeryAdapter.MyViewHolder> {

    private final List<Surgery> surgeries;
    private final Activity activity;


    public SurgeryAdapter(ArrayList<Surgery> data, Activity activity) {

        this.surgeries = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.surgery_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Surgery surgery = surgeries.get(position) ;


        Timestamp timestamp = surgery.getTimestamp();
        String title = surgery.getTitle();
        Date date = timestamp.toDate();

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        holder.date.setText(title + " at: " + format.format(date));



        holder.itemView.setOnClickListener(view -> {
            //doctorsFragment.openSheet(labTestRecords)
        });



    }

    @Override
    public int getItemCount() {
        return surgeries.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date;


        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.surgery_title);

        }
    }


}
