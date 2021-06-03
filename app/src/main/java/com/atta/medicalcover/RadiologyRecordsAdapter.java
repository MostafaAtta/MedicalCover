package com.atta.medicalcover;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.RadiologyFragmentDirections;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RadiologyRecordsAdapter extends RecyclerView.Adapter<RadiologyRecordsAdapter.MyViewHolder> {

    private final List<RadiologyRecord> radiologyRecords;
    private final Activity activity;


    public RadiologyRecordsAdapter(ArrayList<RadiologyRecord> data, Activity activity) {

        this.radiologyRecords = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.radiology_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final RadiologyRecord radiologyRecord = radiologyRecords.get(position) ;


        Timestamp timestamp = radiologyRecord.getTimestamp();
        String title = radiologyRecord.getTitle();
        Date date = timestamp.toDate();

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        holder.date.setText(title + " Results for: " + format.format(date));



        holder.itemView.setOnClickListener(view ->
            Navigation.findNavController(view)
                    .navigate(RadiologyFragmentDirections.actionRadiologyFragmentToRadiologyDetailsFragment(radiologyRecord))
        );



    }

    @Override
    public int getItemCount() {
        return radiologyRecords.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date;


        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.radiology_title);

        }
    }


}
