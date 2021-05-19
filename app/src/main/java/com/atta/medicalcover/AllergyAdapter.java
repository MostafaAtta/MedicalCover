package com.atta.medicalcover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.AllergyFragment;

import java.util.ArrayList;
import java.util.List;

public class AllergyAdapter extends RecyclerView.Adapter<AllergyAdapter.MyViewHolder> {

    private final List<Allergy> allergies;
    private final AllergyFragment allergyFragment;


    public AllergyAdapter(ArrayList<Allergy> data, AllergyFragment allergyFragment) {

        this.allergies = data;
        this.allergyFragment = allergyFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.allergy_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Allergy allergy = allergies.get(position) ;

        holder.allergyTitle.setText(allergy.getName());



        holder.delete.setOnClickListener(view ->{
            allergyFragment.deleteAllergy(position);
            notifyDataSetChanged();
        });



    }

    @Override
    public int getItemCount() {
        return allergies.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView allergyTitle;

        ImageView delete;


        MyViewHolder(View view) {
            super(view);
            allergyTitle = view.findViewById(R.id.allergy_title);
            delete = view.findViewById(R.id.delete_allergy);

        }
    }


}
