package com.atta.medicalcover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.VaccineFragment;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.MyViewHolder> {

    private final List<Vaccine> vaccines;
    private final VaccineFragment vaccineFragment;


    public VaccineAdapter(ArrayList<Vaccine> data, VaccineFragment vaccineFragment) {

        this.vaccines = data;
        this.vaccineFragment = vaccineFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vaccine_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Vaccine vaccine = vaccines.get(position) ;

        holder.vaccineTitle.setText(vaccine.getName());

        Timestamp timestamp = vaccine.getTimestamp();

        Date date = timestamp.toDate();

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        holder.vaccineDate.setText(format.format(date));

        holder.delete.setOnClickListener(view ->{
            vaccineFragment.deleteAllergy(position);
            //notifyDataSetChanged();
        });



    }

    @Override
    public int getItemCount() {
        return vaccines.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView vaccineTitle, vaccineDate;

        ImageView delete;


        MyViewHolder(View view) {
            super(view);
            vaccineTitle = view.findViewById(R.id.vaccine_title);
            vaccineDate = view.findViewById(R.id.vaccine_date);
            delete = view.findViewById(R.id.delete_vaccine);

        }
    }


}
