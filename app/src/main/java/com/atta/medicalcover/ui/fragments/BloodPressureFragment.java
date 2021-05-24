package com.atta.medicalcover.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.atta.medicalcover.BloodPressure;
import com.atta.medicalcover.BloodPressureAdapter;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
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


public class BloodPressureFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    Button addBloodPressure;

    EditText systolicTxt, diastolicTxt, pulseTxt;

    RecyclerView bloodPressureRecycler;

    BloodPressureAdapter myAdapter;

    ArrayList<BloodPressure> bloodPressures;

    ImageView calenderImg;

    TextView dateTv;

    Date date;

    Timestamp timestamp;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener dateSetListener;

    TimePickerDialog.OnTimeSetListener timeSetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_blood_pressure, container, false);

        initiateViews();

        return root;
    }


    private void initiateViews() {

        addBloodPressure = root.findViewById(R.id.add_blood_pressure_btn);
        systolicTxt = root.findViewById(R.id.systolic);
        diastolicTxt = root.findViewById(R.id.diastolic);
        pulseTxt = root.findViewById(R.id.pulse);

        addBloodPressure.setOnClickListener(view ->
                addBloodPressure(systolicTxt.getText().toString().trim(),
                        diastolicTxt.getText().toString().trim(),
                        pulseTxt.getText().toString().trim())
        );

        bloodPressureRecycler = root.findViewById(R.id.blood_pressure_recyclerView);

        db = FirebaseFirestore.getInstance();

        getBloodPressure();


        dateTv = root.findViewById(R.id.blood_pressure_date_tv);
        date = new Date();
        String pattern = "EEE, dd MMM hh:mm a";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        dateTv.setText("Date: " + format.format(date));


        myCalendar = Calendar.getInstance();

        dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            viewTimeDialog();
        };

        timeSetListener = (view, hourOfDay, minute) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);


            String myFormat = "MMMM d, yyyy hh:mm a"; //In which you need put here
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

    private void viewTimeDialog() {

        new TimePickerDialog(getContext(), timeSetListener, myCalendar
                .get(Calendar.HOUR_OF_DAY), myCalendar
                .get(Calendar.MINUTE), false).show();

    }

    private void addBloodPressure(String systolic, String diastolic, String pulse) {

        timestamp = new Timestamp(date);

        Map<String, Object> bloodPressureRecord = new HashMap<>();
        bloodPressureRecord.put("systolic", systolic);
        bloodPressureRecord.put("diastolic", diastolic);
        bloodPressureRecord.put("pulse", pulse);
        bloodPressureRecord.put("timestamp", timestamp);

        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Blood Pressure")
                .document()
                .set(bloodPressureRecord)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    if (bloodPressures != null)
                        bloodPressures.clear();
                    getBloodPressure();

                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    public void getBloodPressure(){
        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Blood Pressure")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        bloodPressures = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            BloodPressure bloodPressure = documentSnapshot.toObject(BloodPressure.class);
                            bloodPressure.setId(documentSnapshot.getId());
                            bloodPressures.add(bloodPressure);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(bloodPressures);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<BloodPressure> bloodPressures) {
        myAdapter = new BloodPressureAdapter(bloodPressures, this);

        bloodPressureRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        bloodPressureRecycler.setAdapter(myAdapter);
    }

    public void deleteAllergy(int i) {

        bloodPressures.remove(i);
    }
}