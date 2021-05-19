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
import com.atta.medicalcover.R;
import com.atta.medicalcover.TestCenter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CenterDetailsFragment extends Fragment {

    View root;

    TestCenter testCenter;

    TextView centerName, phoneTxt;

    ImageView centerImg;

    Button addRequest;

    RecyclerView branchesRecycler;

    FirebaseFirestore db;

    BranchesAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_center_details, container, false);

        testCenter = CenterDetailsFragmentArgs.fromBundle(getArguments()).getCenter();

        db = FirebaseFirestore.getInstance();
        initiateViews();

        return root;
    }


    private void initiateViews(){

        centerName = root.findViewById(R.id.radiology_title);
        centerImg = root.findViewById(R.id.center_img);
        phoneTxt = root.findViewById(R.id.phone_txt);
        branchesRecycler = root.findViewById(R.id.branches_recycler);

        centerName.setText(testCenter.getName());
        phoneTxt.setText(testCenter.getPhone());
        phoneTxt.setOnClickListener(v -> {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
            phoneIntent.setData(Uri.parse("tel:"+testCenter.getPhone()));
            getActivity().startActivity(phoneIntent);
        });

        Picasso.get()
                .load(testCenter.getImage())
                .resize(80, 80)
                .centerCrop()
                .into(centerImg);

        addRequest = root.findViewById(R.id.add_request);
        addRequest.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(CenterDetailsFragmentDirections
                                .actionCenterDetailsFragmentToNavigationVisits(null, testCenter))
        );

        getBranches();
    }

    private void getBranches() {

        db.collection("Services")
                .document(testCenter.getId())
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


    private void showRecycler(ArrayList<Branch> data) {

        myAdapter = new BranchesAdapter(data, getActivity());

        branchesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        branchesRecycler.setAdapter(myAdapter);
    }

}