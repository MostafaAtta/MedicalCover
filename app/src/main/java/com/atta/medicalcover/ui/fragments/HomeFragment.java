package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.R;
import com.atta.medicalcover.SpecialtiesAdapter;
import com.atta.medicalcover.Specialty;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    View root;

    FirebaseFirestore db;

    RecyclerView recyclerView;

    SpecialtiesAdapter myAdapter;

    TextView moreTv;

    CardView internalCardView, externalCardView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.specialties_recyclerView);

        moreTv = root.findViewById(R.id.tv_more);
        internalCardView = root.findViewById(R.id.internalCardView);
        externalCardView = root.findViewById(R.id.externalCardView);

        moreTv.setOnClickListener(this);
        internalCardView.setOnClickListener(this);
        externalCardView.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

        getTopSpecialties();

        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == moreTv){
            Navigation.findNavController(view)
                    .navigate(HomeFragmentDirections.actionNavigationHomeToSpecialtiesFragment(null));
        }else if(view == externalCardView){

            Navigation.findNavController(view)
                    .navigate(HomeFragmentDirections.actionNavigationHomeToSpecialtiesFragment("External"));
        }else if(view == internalCardView){

            Navigation.findNavController(view)
                    .navigate(HomeFragmentDirections.actionNavigationHomeToSpecialtiesFragment("Internal"));
        }
    }

    public void getTopSpecialties(){

        db.collection("Specialties")
                .whereEqualTo("top", true)
                .orderBy("order")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){

                            ArrayList<Specialty> data = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                data.add(documentSnapshot.toObject(Specialty.class));

                                //add(documentSnapshot.toObject(Specialty.class));
                            }

                            showRecycler(data);
                        }else {
                            Toast.makeText(getContext(), "User not found, Please Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void add(Specialty specialty){

        db.collection("Specialties").document().set(specialty);
    }

    private void showRecycler(ArrayList<Specialty> data) {

        myAdapter = new SpecialtiesAdapter(data, getActivity(), true, null);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(myAdapter);
    }
}