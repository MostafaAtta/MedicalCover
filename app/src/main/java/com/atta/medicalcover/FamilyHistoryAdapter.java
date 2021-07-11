package com.atta.medicalcover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.FamilyHistoryFragment;

import java.util.ArrayList;
import java.util.List;

public class FamilyHistoryAdapter extends RecyclerView.Adapter<FamilyHistoryAdapter.MyViewHolder> {

    private final List<FamilyHistory> familyHistories;
    private final FamilyHistoryFragment familyHistoryFragment;


    public FamilyHistoryAdapter(ArrayList<FamilyHistory> data, FamilyHistoryFragment familyHistoryFragment) {

        this.familyHistories = data;
        this.familyHistoryFragment = familyHistoryFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.family_history_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final FamilyHistory familyHistory = familyHistories.get(position) ;

        holder.familyMember.setText(familyHistory.getMember());
        holder.description.setText(familyHistory.getDescription());



        holder.delete.setOnClickListener(view ->{
            familyHistoryFragment.deleteAllergy(position);
            //notifyDataSetChanged();
        });



    }

    @Override
    public int getItemCount() {
        return familyHistories.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView familyMember, description;

        ImageView delete;


        MyViewHolder(View view) {
            super(view);
            familyMember = view.findViewById(R.id.family_member);
            description = view.findViewById(R.id.description);
            delete = view.findViewById(R.id.delete_record);

        }
    }


}
