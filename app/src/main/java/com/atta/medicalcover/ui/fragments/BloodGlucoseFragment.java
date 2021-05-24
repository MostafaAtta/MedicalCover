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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.BloodGlucose;
import com.atta.medicalcover.BloodGlucoseAdapter;
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

public class BloodGlucoseFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    Button addBloodGlucose;

    EditText resultTxt;

    RecyclerView bloodGlucoseRecycler;

    BloodGlucoseAdapter myAdapter;

    ArrayList<BloodGlucose> bloodGlucoseRecords;

    RadioGroup typesRadioGroup;

    ImageView calenderImg;

    TextView dateTv;

    String type;

    Date date;

    Timestamp timestamp;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener dateSetListener;

    TimePickerDialog.OnTimeSetListener timeSetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_blood_glucose, container, false);

        initiateViews();

        return root;
    }

    private void initiateViews() {

        addBloodGlucose = root.findViewById(R.id.add_blood_glucose_btn);
        resultTxt = root.findViewById(R.id.result);

        addBloodGlucose.setOnClickListener(view ->
                addBloodGlucose(resultTxt.getText().toString().trim())
        );

        bloodGlucoseRecycler = root.findViewById(R.id.blood_glucose_recyclerView);

        typesRadioGroup = root.findViewById(R.id.type_radioGroup);
        typesRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.fasting_radioButton){
                type = getActivity().getResources().getString(R.string.fasting);
            }else if (checkedId == R.id.postprandial_radioButton){
                type = getActivity().getResources().getString(R.string.postprandial);
            }else if (checkedId == R.id.random_radioButton){
                type = getActivity().getResources().getString(R.string.random);
            }
        });

        db = FirebaseFirestore.getInstance();

        getBloodGlucose();


        dateTv = root.findViewById(R.id.blood_glucose_date_tv);
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

    private void addBloodGlucose(String result) {

        timestamp = new Timestamp(date);

        Map<String, Object> bloodGlucoseRecord = new HashMap<>();
        bloodGlucoseRecord.put("result", result);
        bloodGlucoseRecord.put("type", type);
        bloodGlucoseRecord.put("timestamp", timestamp);

        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Blood Glucose")
                .document()
                .set(bloodGlucoseRecord)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    if (bloodGlucoseRecords != null)
                        bloodGlucoseRecords.clear();
                    getBloodGlucose();

                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    public void getBloodGlucose(){
        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .collection("Blood Glucose")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        bloodGlucoseRecords = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            BloodGlucose bloodGlucose = documentSnapshot.toObject(BloodGlucose.class);
                            bloodGlucose.setId(documentSnapshot.getId());
                            bloodGlucoseRecords.add(bloodGlucose);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(bloodGlucoseRecords);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<BloodGlucose> bloodGlucoses) {
        myAdapter = new BloodGlucoseAdapter(bloodGlucoses, this);

        bloodGlucoseRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        bloodGlucoseRecycler.setAdapter(myAdapter);
    }

    public void deleteAllergy(int i) {

        bloodGlucoseRecords.remove(i);
    }
}