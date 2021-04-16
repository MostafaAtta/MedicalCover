package com.atta.medicalcover;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.HomeFragmentDirections;
import com.atta.medicalcover.ui.fragments.SpecialtiesFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SpecialtiesAdapter extends RecyclerView.Adapter<SpecialtiesAdapter.MyViewHolder> {

    private List<Specialty> specialties;
    private Activity activity;
    private boolean homeView;
    private String type;

    public SpecialtiesAdapter(ArrayList<Specialty> data, Activity activity,
                              boolean homeView, String type) {

        this.specialties = data;
        this.activity = activity;
        this.homeView = homeView;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (homeView){

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.specialties_item_layout, parent, false);
        }else {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.specialties_item_layout2, parent, false);
        }


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Specialty specialty = specialties.get(position) ;

        final String imageUrl = specialty.getImage();
        final String name = specialty.getName();

        holder.specialityName.setText(name);


        if (specialty.getImage() != null) {

            if (homeView){
                Picasso.get()
                        .load(imageUrl)
                        .resize(80, 80)
                        .centerCrop()
                        .into(holder.specialityImage);
            }else {
                Picasso.get()
                        .load(imageUrl)
                        .resize(40, 40)
                        .centerCrop()
                        .into(holder.specialityImage);
            }

        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeView) {
                    Navigation
                            .findNavController(activity, R.id.nav_host_fragment)
                            .navigate(HomeFragmentDirections.actionNavigationHomeToDoctorsFragment(specialty.getName(), type));
                }else {
                    Navigation
                            .findNavController(activity, R.id.nav_host_fragment)
                            .navigate(SpecialtiesFragmentDirections.actionSpecialtiesFragmentToDoctorsFragment(specialty.getName(), type));
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return specialties.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView specialityName;
        ImageView specialityImage;

        MyViewHolder(View view) {
            super(view);
            specialityName = view.findViewById(R.id.specialty_name);
            specialityImage = view.findViewById(R.id.imageView);

        }
    }


}
