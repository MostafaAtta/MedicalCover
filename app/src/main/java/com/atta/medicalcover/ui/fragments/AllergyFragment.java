package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.Allergy;
import com.atta.medicalcover.AllergyAdapter;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllergyFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    Button addAllergy;

    EditText newAllergy;

    RecyclerView allergyRecycler;

    AllergyAdapter myAdapter;

    ArrayList<Allergy> allergies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_allergy, container, false);

        initiateViews();

        return root;
    }


    private void initiateViews() {

        addAllergy = root.findViewById(R.id.add_allergy_btn);
        newAllergy = root.findViewById(R.id.add_allergy);

        addAllergy.setOnClickListener(view ->
                addAllergy(newAllergy.getText().toString().trim())
                );

        allergyRecycler = root.findViewById(R.id.allergy_recyclerView);

        db = FirebaseFirestore.getInstance();

        getAllergy();
    }

    private void addAllergy(String allergy) {

        Map<String, Object> allergyRecord = new HashMap<>();
        allergyRecord.put("allergy", allergy);

        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Allergy")
                .document()
                .set(allergyRecord)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    if (allergies != null)
                        allergies.clear();
                    getAllergy();

                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    public void getAllergy(){
        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Allergy")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        allergies = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            String allergy = documentSnapshot.getString("allergy");
                            allergies.add(new Allergy(documentSnapshot.getId(), allergy));
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(allergies);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<Allergy> allergies) {
        myAdapter = new AllergyAdapter(allergies, this);

        allergyRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        allergyRecycler.setAdapter(myAdapter);
    }

    public void deleteAllergy(int i) {

        allergies.remove(i);
    }
}