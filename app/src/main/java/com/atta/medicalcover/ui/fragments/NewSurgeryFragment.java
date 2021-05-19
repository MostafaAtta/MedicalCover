package com.atta.medicalcover.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.atta.medicalcover.Doctor;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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

public class NewSurgeryFragment extends Fragment {

    View root;

    ImageView calenderImg;

    TextInputEditText resultText, placeText;

    EditText titleText;

    AutoCompleteTextView doctorText;

    TextView dateTv;

    Button saveBtn;

    FirebaseFirestore db;

    Date date;

    Timestamp timestamp;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener dateSetListener;


    ArrayList<Doctor> doctors = new ArrayList<>();
    ArrayList<String> doctorsName = new ArrayList<>();
    Doctor selectedDoctor = new Doctor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_new_surgery, container, false);

        initiateViews();

        return root;
    }


    private void initiateViews() {

        db = FirebaseFirestore.getInstance();

        getDoctors();

        dateTv = root.findViewById(R.id.surgery_date_tv);
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

        titleText = root.findViewById(R.id.surgery_title);
        resultText = root.findViewById(R.id.surgery_result);
        doctorText = root.findViewById(R.id.surgery_doctor);
        placeText = root.findViewById(R.id.surgery_place);

        saveBtn = root.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(v -> {

            saveRecord();

        });
    }

    private void viewCalender() {

        new DatePickerDialog(getContext(), dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }



    private void saveRecord() {

        timestamp = new Timestamp(date);

        String doctorName = doctorText.getText().toString().trim();
        if (selectedDoctor.getName() != null){
            if (!doctorName.equals(selectedDoctor.getName())){
                selectedDoctor = new Doctor();
            }
        }

        Map<String, Object> surgeryRecord = new HashMap<>();
        surgeryRecord.put("title", titleText.getText().toString().trim());
        surgeryRecord.put("result", resultText.getText().toString().trim());
        surgeryRecord.put("doctorId", selectedDoctor.getId());
        surgeryRecord.put("doctorName", doctorText.getText().toString().trim());
        surgeryRecord.put("centerName", placeText.getText().toString().trim());
        surgeryRecord.put("timestamp", timestamp);
        surgeryRecord.put("userId", SessionManager.getInstance(getContext()).getUserId());

        db.collection("Surgery").document().set(surgeryRecord)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();

                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                                .navigate(NewSurgeryFragmentDirections.actionNewSurgeryFragmentToNavigationHome());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "An error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void getDoctors(){

        db.collection("Doctors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){


                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Doctor doctor = documentSnapshot.toObject(Doctor.class);
                            doctor.setId(documentSnapshot.getId());
                            doctors.add(doctor);
                            doctorsName.add(doctor.getName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.select_dialog_item, doctorsName);
                        doctorText.setThreshold(1);//will start working from first character
                        doctorText.setAdapter(adapter);
                        doctorText.setOnItemClickListener((parent, view, position, id) ->
                                selectedDoctor = doctors.get(position)
                        );


                    }else {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }


}