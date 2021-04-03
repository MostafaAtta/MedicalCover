package com.atta.medicalcover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.BookingFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlotsAdapter extends RecyclerView.Adapter<SlotsAdapter.MyViewHolder> {

    private final List<String> slots;
    private final BookingFragment bookingFragment;
    private final Context context;
    public SlotsAdapter(ArrayList<String> data, BookingFragment bookingFragment, Context context) {

        this.slots = data;
        this.bookingFragment = bookingFragment;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slots_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String timeSlot = slots.get(position);
        String convertedTimeSlot;
        int hours = Integer.parseInt(Arrays.asList(timeSlot.split(":")).get(0));
        String min = Arrays.asList(timeSlot.split(":")).get(1);
        if (hours <= 12 && hours > 0){
            convertedTimeSlot = timeSlot + " AM";
        }else {
            hours -= 12;
            if (hours < 10){

                convertedTimeSlot = "0" + hours + ":" + min + " PM";
            }else {

                convertedTimeSlot = hours + ":" + min + " PM";
            }
        }
        holder.slotName.setText(convertedTimeSlot);





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingFragment.addAppointments(slots.get(position));
            }
        });



    }

    @Override
    public int getItemCount() {
        return slots.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView slotName;

        MyViewHolder(View view) {
            super(view);
            slotName = view.findViewById(R.id.slot_tv);
        }
    }


}
