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

import com.atta.medicalcover.FamilyHistory;
import com.atta.medicalcover.FamilyHistoryAdapter;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FamilyHistoryFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    Button addFamilyHistory;

    EditText familyMember, description;

    RecyclerView familyHistoryRecycler;

    FamilyHistoryAdapter myAdapter;

    ArrayList<FamilyHistory> familyHistories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_family_history, container, false);

        initiateViews();

        return root;
    }



    private void initiateViews() {

        addFamilyHistory = root.findViewById(R.id.add_family_history_btn);
        familyMember = root.findViewById(R.id.family_member);
        description = root.findViewById(R.id.description);

        addFamilyHistory.setOnClickListener(view ->
                addFamilyHistory(familyMember.getText().toString().trim(),
                        description.getText().toString().trim())
        );

        familyHistoryRecycler = root.findViewById(R.id.family_history_recyclerView);

        db = FirebaseFirestore.getInstance();

        getFamilyHistories();
    }

    private void addFamilyHistory(String familyMember, String description) {

        Map<String, Object> allergyRecord = new HashMap<>();
        allergyRecord.put("member", familyMember);
        allergyRecord.put("description", description);

        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Family Member")
                .document()
                .set(allergyRecord)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    if (familyHistories != null)
                        familyHistories.clear();
                    getFamilyHistories();

                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    public void getFamilyHistories(){
        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Family Member")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        familyHistories = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            FamilyHistory familyHistory = documentSnapshot.toObject(FamilyHistory.class);
                            familyHistory.setId(documentSnapshot.getId());
                            familyHistories.add(familyHistory);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(familyHistories);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<FamilyHistory> familyHistories) {
        myAdapter = new FamilyHistoryAdapter(familyHistories, this);

        familyHistoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        familyHistoryRecycler.setAdapter(myAdapter);
    }

    public void deleteAllergy(int i) {

        familyHistories.remove(i);
    }
}