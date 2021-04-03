package com.atta.medicalcover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.BookingFragment;
import com.atta.medicalcover.ui.fragments.DoctorsFragmentDirections;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.MyViewHolder> {

    private List<String> dates;
    private BookingFragment bookingFragment;
    private Context context;
    int selectedPosition = 0;
    public DatesAdapter(ArrayList<String> data, BookingFragment bookingFragment, Context context) {

        this.dates = data;
        this.bookingFragment = bookingFragment;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.days_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.dateName.setText(dates.get(position));

        if(selectedPosition==position){


            bookingFragment.setSelectedDate(dates.get(position), position);
            holder.daysCardView.setBackground(ContextCompat.getDrawable(context, R.drawable.card_border2));
        }
        else{

            holder.daysCardView.setBackground(ContextCompat.getDrawable(context,R.drawable.card_border));
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedPosition=position;
                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {
        return dates.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateName;
        View daysCardView;

        MyViewHolder(View view) {
            super(view);
            dateName = view.findViewById(R.id.day_tv);
            daysCardView = view.findViewById(R.id.view3);
        }
    }


}
