package com.atta.medicalcover;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class BranchesAdapter extends RecyclerView.Adapter<BranchesAdapter.MyViewHolder> {

    private List<Branch> branches;
    FragmentActivity activity;

    public BranchesAdapter(ArrayList<Branch> data, FragmentActivity activity) {

        this.branches = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.branchs_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Branch branch = branches.get(position) ;

        holder.branchName.setText(branch.getName());
        holder.branchAddress.setText(branch.getAddress());

        holder.itemView.setOnClickListener(view ->
                openMapLocation(branch.getLocation(), branch)
        );

        holder.imageView.setOnClickListener(v ->
                openMapLocation(branch.getLocation(), branch)
        );
    }

    private void openMapLocation(GeoPoint location, Branch branch){

        Uri uri = Uri.parse("geo:0,0?q=" + location.getLatitude() + "," + location.getLongitude() + "("+ branch.getName() + " Branch)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(uri);
        mapIntent.setPackage("com.google.android.apps.maps");

        activity.startActivity(mapIntent);
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView branchName, branchAddress;
        ImageView imageView;

        MyViewHolder(View view) {
            super(view);
            branchName = view.findViewById(R.id.branch_name);
            branchAddress = view.findViewById(R.id.branch_address);
            imageView = view.findViewById(R.id.location_img);

        }
    }




}
