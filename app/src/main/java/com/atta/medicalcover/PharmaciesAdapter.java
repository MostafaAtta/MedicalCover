package com.atta.medicalcover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.MedicationsRequestFragment;
import com.atta.medicalcover.ui.fragments.PharmaciesFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PharmaciesAdapter extends RecyclerView.Adapter<PharmaciesAdapter.MyViewHolder> {

    private List<Pharmacy> pharmacies;
    private List<Pharmacy> pharmaciesFullList;
    private MedicationsRequestFragment medFragment;

    public PharmaciesAdapter(ArrayList<Pharmacy> data, MedicationsRequestFragment medFragment) {
        this.pharmacies = data;
        this.medFragment = medFragment;
        pharmaciesFullList = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;

        if (medFragment == null){

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pharmacies_item_layout2, parent, false);
        }else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pharmacies_item_layout, parent, false);
        }


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Pharmacy pharmacy = pharmacies.get(position) ;

        final String name = pharmacy.getName();

        holder.pharmacyName.setText(name);

        if (medFragment != null) {

            holder.itemView.setOnClickListener(view -> medFragment.addMedicationsRequest(pharmacy));
        }else {
            holder.itemView.setOnClickListener(view ->
                    Navigation.findNavController(holder.itemView)
                    .navigate(PharmaciesFragmentDirections
                            .actionNavigationPharmaciesToPharmacyDetailsFragment(pharmacy, pharmacy.getName()))
            );

            Picasso.get()
                    .load(pharmacy.getImage())
                    .placeholder(R.drawable.ic_pharmacy)
                    .resize(80, 80)
                    .centerCrop()
                    .into(holder.imageView);

        }



    }

    @Override
    public int getItemCount() {
        return pharmacies.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName;
        ImageView imageView;

        MyViewHolder(View view) {
            super(view);
            pharmacyName = view.findViewById(R.id.pharmacy_name);
            imageView = view.findViewById(R.id.pharmacy_img);

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
