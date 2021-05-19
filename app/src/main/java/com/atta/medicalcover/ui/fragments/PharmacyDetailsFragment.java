package com.atta.medicalcover.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.Branch;
import com.atta.medicalcover.BranchesAdapter;
import com.atta.medicalcover.Pharmacy;
import com.atta.medicalcover.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PharmacyDetailsFragment extends Fragment {

    View root;

    Pharmacy pharmacy;

    TextView pharmacyName, phoneTxt;

    ImageView pharmacyImg;

    Button addRequest;

    RecyclerView branchesRecycler;

    FirebaseFirestore db;

    BranchesAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_pharmacy_details, container, false);


        pharmacy = PharmacyDetailsFragmentArgs.fromBundle(getArguments()).getPharmacy();

        db = FirebaseFirestore.getInstance();
        initiateViews();

        return root;
    }


    private void initiateViews(){

        pharmacyName = root.findViewById(R.id.pharmacy_name);
        pharmacyImg = root.findViewById(R.id.pharmacy_img);
        phoneTxt = root.findViewById(R.id.phone_txt);
        branchesRecycler = root.findViewById(R.id.branches_recycler);

        pharmacyName.setText(pharmacy.getName());
        phoneTxt.setText(pharmacy.getPhone());
        phoneTxt.setOnClickListener(v -> {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
            phoneIntent.setData(Uri.parse("tel:"+pharmacy.getPhone()));
            getActivity().startActivity(phoneIntent);
        });

        Picasso.get()
                .load(pharmacy.getImage())
                .resize(80, 80)
                .centerCrop()
                .into(pharmacyImg);

        addRequest = root.findViewById(R.id.add_request);
        addRequest.setOnClickListener(v ->
            Navigation.findNavController(v)
                    .navigate(PharmacyDetailsFragmentDirections
                            .actionPharmacyDetailsFragmentToNavigationVisits(pharmacy, null))
        );

        getBranches();
    }

    private void getBranches() {

        db.collection("Pharmacies")
                .document(pharmacy.getId())
                .collection("Branches")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        ArrayList<Branch> data = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            data.add(documentSnapshot.toObject(Branch.class));

                            //add(documentSnapshot.toObject(Branch.class));
                        }

                        showRecycler(data);
                    }else {
                        Toast.makeText(getContext(), "User not found, Please Register", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }


    private void add(Branch branch){

        db.collection("Pharmacies")
                .document(pharmacy.getId())
                .collection("Branches").document().set(branch);
    }


    private void showRecycler(ArrayList<Branch> data) {

        myAdapter = new BranchesAdapter(data, getActivity());

        branchesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        branchesRecycler.setAdapter(myAdapter);
    }




}