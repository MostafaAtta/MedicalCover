package com.atta.medicalcover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.MedicationsRequestFragment;
import com.atta.medicalcover.ui.fragments.TestsRequestFragment;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder> {

    private List<TestCenter> testCenters;
    private List<TestCenter> testCentersFullList;
    private TestsRequestFragment fragment;

    public ServicesAdapter(ArrayList<TestCenter> data, TestsRequestFragment fragment) {

        this.testCenters = data;
        this.fragment = fragment;
        testCentersFullList = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.test_centers_item_layout, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final TestCenter testCenter = testCenters.get(position) ;

        final String name = testCenter.getName();
        final String type = testCenter.getType();

        holder.centerName.setText(name);

        holder.centerType.setText(type);




        holder.itemView.setOnClickListener(view -> fragment.addTestRequest(testCenter));



    }

    @Override
    public int getItemCount() {
        return testCenters.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView centerName, centerType;

        MyViewHolder(View view) {
            super(view);
            centerName = view.findViewById(R.id.center_name);
            centerType = view.findViewById(R.id.center_type);

        }
    }



    public Filter getFilter() {
        return pharmaciesFilter;
    }

    private Filter pharmaciesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TestCenter> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(testCentersFullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TestCenter item : testCentersFullList) {
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
            testCenters.clear();
            testCenters.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}
