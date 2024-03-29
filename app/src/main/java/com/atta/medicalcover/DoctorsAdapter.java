package com.atta.medicalcover;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.DoctorsFragment;
import com.atta.medicalcover.ui.fragments.DoctorsFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.MyViewHolder> {

    private final List<Doctor> doctors;
    private final Activity activity;
    private final DoctorsFragment doctorsFragment;


    public DoctorsAdapter(ArrayList<Doctor> data, Activity activity,
                          DoctorsFragment doctorsFragment) {

        this.doctors = data;
        this.activity = activity;
        this.doctorsFragment = doctorsFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.doctor_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Doctor doctor = doctors.get(position) ;


        final String imageUrl = doctor.getImage();
        final String name = doctor.getName();
        final String experience = doctor.getExperience();
        final String waitingTime = doctor.getWaitingTime();
        final ArrayList<String> reviews = doctor.getReviews();
        final ArrayList<String> degrees = doctor.getDegrees();
        final ArrayList<String> specialities = doctor.getSpecialities();
        final ArrayList<Boolean> satisfied = doctor.getSatisfied();

        int sizeOfSatisfied = satisfied.size();
        int noOfSatisfied = 0;
        for (boolean s: satisfied) {
            if (s){
                noOfSatisfied++;
            }
        }

        int satisfactionPercentage = (int) ( ( (float) noOfSatisfied/sizeOfSatisfied)*100 );

        String satisfactionPercent = satisfactionPercentage + "% (" + sizeOfSatisfied + ")";

        holder.doctorName.setText(name);
        holder.experience.setText(experience);
        holder.waitingTime.setText(waitingTime);
        holder.specialities.setText(String.join(", ", specialities));
        holder.degrees.setText(String.join(", ", degrees));
        holder.reviews.setText(reviews.get(reviews.size()-1));
        holder.satisfied.setText(satisfactionPercent);


        if (doctor.getImage() != null) {

                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .resize(80, 80)
                        .centerCrop()
                        .into(holder.doctorImage);


        }



        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctorsFragment.openSheet(doctor);
            }
        });

        holder.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(activity, R.id.nav_host_fragment)
                        .navigate(DoctorsFragmentDirections.actionDoctorsFragmentToDoctorDetailsFragment(doctor));
            }
        });



    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView doctorName, specialities, degrees, waitingTime, experience, satisfied, reviews;
        ImageView doctorImage;
        Button viewProfile, book;


        MyViewHolder(View view) {
            super(view);
            doctorName = view.findViewById(R.id.doctor_name_tv);
            doctorImage = view.findViewById(R.id.doctor_image);
            specialities = view.findViewById(R.id.clinic_tv);
            degrees = view.findViewById(R.id.degrees_tv);
            waitingTime = view.findViewById(R.id.wait_time_tv);
            experience = view.findViewById(R.id.experience_tv);
            satisfied = view.findViewById(R.id.satisfied_tv);
            reviews = view.findViewById(R.id.reviews_tv);
            viewProfile = view.findViewById(R.id.view_profile_btn);
            book = view.findViewById(R.id.book_btn);

        }
    }


}
