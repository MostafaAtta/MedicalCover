package com.atta.medicalcover.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.atta.medicalcover.Vaccine;
import com.atta.medicalcover.VaccineAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VaccineFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    Button addVaccine;

    EditText newVaccine;

    RecyclerView vaccineRecycler;

    VaccineAdapter myAdapter;

    ArrayList<Vaccine> vaccines;

    ImageView calenderImg;

    TextView dateTv;

    Date date;

    Timestamp timestamp;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener dateSetListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_vaccine, container, false);

        initiateViews();

        return root;
    }


    private void initiateViews() {

        addVaccine = root.findViewById(R.id.add_vaccine_btn);
        newVaccine = root.findViewById(R.id.add_vaccine);

        addVaccine.setOnClickListener(view ->
                addVaccine(newVaccine.getText().toString().trim())
        );

        vaccineRecycler = root.findViewById(R.id.allergy_recyclerView);

        db = FirebaseFirestore.getInstance();

        getVaccine();


        dateTv = root.findViewById(R.id.vaccine_date_tv);
        date = new Date();
        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        dateTv.setText("Date: " + format.format(date));


        myCalendar = Calendar.getInstance();

        dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "MMMM d, yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            date = myCalendar.getTime();
            dateTv.setText(sdf.format(myCalendar.getTime()));
        };


        calenderImg = root.findViewById(R.id.calender_img);
        calenderImg.setOnClickListener(view ->
                viewCalender()
        );
    }


    private void viewCalender() {

        new DatePickerDialog(getContext(), dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void addVaccine(String vaccine) {

        timestamp = new Timestamp(date);

        Map<String, Object> allergyRecord = new HashMap<>();
        allergyRecord.put("name", vaccine);
        allergyRecord.put("timestamp", timestamp);

        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Vaccine")
                .document()
                .set(allergyRecord)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    if (vaccines != null)
                        vaccines.clear();
                    getVaccine();

                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    public void getVaccine(){
        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Vaccine")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        vaccines = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Vaccine vaccine = documentSnapshot.toObject(Vaccine.class);
                            vaccine.setId(documentSnapshot.getId());
                            vaccines.add(vaccine);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(vaccines);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<Vaccine> vaccines) {
        myAdapter = new VaccineAdapter(vaccines, this);

        vaccineRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        vaccineRecycler.setAdapter(myAdapter);
    }

    public void deleteAllergy(int i) {

        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Vaccine")
                .document(vaccines.get(i).getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    vaccines.remove(i);
                    myAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}