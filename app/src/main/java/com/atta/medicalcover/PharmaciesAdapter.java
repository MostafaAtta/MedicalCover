package com.atta.medicalcover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.MedicationsRequestFragment;

import java.util.ArrayList;
import java.util.List;

public class PharmaciesAdapter extends RecyclerView.Adapter<PharmaciesAdapter.MyViewHolder> {

    private List<Pharmacy> pharmacies;
    private List<Pharmacy> pharmaciesFullList;
    private MedicationsRequestFragment fragment;

    public PharmaciesAdapter(ArrayList<Pharmacy> data, MedicationsRequestFragment fragment) {

        this.pharmacies = data;
        this.fragment = fragment;
        pharmaciesFullList = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pharmacies_item_layout, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Pharmacy pharmacy = pharmacies.get(position) ;

        final String name = pharmacy.getName();

        holder.pharmacyName.setText(name);





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.addMedicationsRequest(pharmacy);
            }
        });



    }

    @Override
    public int getItemCount() {
        return pharmacies.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName;

        MyViewHolder(View view) {
            super(view);
            pharmacyName = view.findViewById(R.id.pharmacy_name);

        }
    }



    public Filter getFilter() {
        return pharmaciesFilter;
    }

    private Filter pharmaciesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Pharmacy> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(pharmaciesFullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Pharmacy item : pharmaciesFullList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pharmacies.clear();
            pharmacies.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}
