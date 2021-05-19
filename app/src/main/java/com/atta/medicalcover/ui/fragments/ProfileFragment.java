package com.atta.medicalcover.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.atta.medicalcover.MultiSpinner;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.atta.medicalcover.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment {

    View root;

    Button updateBtn;

    TextInputEditText emailText, nameText, phoneText,
            membershipText, policyHolderText, policyText;

    EditText dateOfBirthText;

    String email, password, fullName, city, phone, membershipNumber, policyHolder, policyNumber,
            dateOfBirth = "", gender = "", bloodType;

    ArrayList<String> selectedChronicDiseases = new ArrayList<>();

    ArrayList<String> cities = new ArrayList<>();

    ArrayList<String> bloodTypes = new ArrayList<>();

    ArrayList<String> chronicDiseases = new ArrayList<>();

    boolean isPregnant, isChronicDiseases;

    RadioGroup genderRadio;

    RadioButton maleBtn, femaleBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String TAG = "ProfileFragment";

    ArrayAdapter<String> cityAdapter, bloodAdapter, chronicDiseasesAdapter;

    Spinner citySpinner, bloodSpinner;

    MultiSpinner chronicDiseasesSpinner;

    CheckBox pregnancyCheckBox, chronicDiseasesCheckBox;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener date;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_profile, container, false);

        initiateViews();

        return root;
    }


    private void initiateViews() {

        db = FirebaseFirestore.getInstance();

        citySpinner = root.findViewById(R.id.location_spinner);
        chronicDiseasesSpinner = root.findViewById(R.id.chronic_diseases_spinner);
        bloodSpinner = root.findViewById(R.id.blood_type_spinner);
        pregnancyCheckBox = root.findViewById(R.id.pregnancy_checkBox);
        pregnancyCheckBox.setVisibility(View.GONE);
        chronicDiseasesCheckBox = root.findViewById(R.id.chronic_diseases_checkBox);

        updateBtn = root.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(v ->
            updateProfile()
        );

        emailText = root.findViewById(R.id.registerEmail);
        emailText.setEnabled(false);
        emailText.setFocusable(false);
        nameText = root.findViewById(R.id.registerName);
        phoneText = root.findViewById(R.id.registerPhone);

        cities.add("Cairo");
        cities.add("Alex");
        cities.add("Giza");
        cities.add("Luxor");
        cities.add("Tanta");

        cityAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                cities);

        citySpinner.setAdapter(cityAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                city = cities.get(0);
            }
        });


        chronicDiseases.add("CARDIOVASCULAR DISEASES");
        chronicDiseases.add("SMOKING-RELATED HEALTH ISSUES");
        chronicDiseases.add("ALCOHOL-RELATED HEALTH ISSUES");
        chronicDiseases.add("DIABETES");
        chronicDiseases.add("ALZHEIMER'S DISEASE");
        chronicDiseases.add("CANCER");
        chronicDiseases.add("OBESITY");
        chronicDiseases.add("ARTHRITIS");
        chronicDiseases.add("ASTHMA");
        chronicDiseases.add("STROKE");

        pregnancyCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isPregnant = isChecked;
        });

        chronicDiseasesCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isChronicDiseases = isChecked;
            if (isChecked){
                chronicDiseasesSpinner.setVisibility(View.VISIBLE);
            }else {

                chronicDiseasesSpinner.setVisibility(View.GONE);
            }
        });

        chronicDiseasesSpinner.setItems(chronicDiseases, getString(R.string.for_all), selected -> {
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]){
                    selectedChronicDiseases.add(chronicDiseases.get(i));
                }
            }
        });
        /*chronicDiseasesAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                chronicDiseases);

        chronicDiseasesSpinner.setAdapter(chronicDiseasesAdapter);

        chronicDiseasesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //city = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //city = cities.get(0);
            }
        });*/

        bloodTypes.add("A+");
        bloodTypes.add("A-");
        bloodTypes.add("B+");
        bloodTypes.add("B-");
        bloodTypes.add("O+");
        bloodTypes.add("O-");
        bloodTypes.add("AB+");
        bloodTypes.add("AB+");


        bloodAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                bloodTypes);

        bloodSpinner.setAdapter(bloodAdapter);

        bloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodType = bloodTypes.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bloodType = bloodTypes.get(0);
            }
        });

        maleBtn = root.findViewById(R.id.maleBtn);
        femaleBtn = root.findViewById(R.id.femaleBtn);
        genderRadio = root.findViewById(R.id.genderRadio);
        genderRadio.setOnCheckedChangeListener((radioGroup, i) -> {

            if (i == R.id.maleBtn) {
                gender = "Male";
                isPregnant = false;
                pregnancyCheckBox.setVisibility(View.GONE);
                root.findViewById(R.id.pregnancyTv).setVisibility(View.GONE);
            }else if (i == R.id.femaleBtn){
                gender = "Female";
                pregnancyCheckBox.setVisibility(View.VISIBLE);
                root.findViewById(R.id.pregnancyTv).setVisibility(View.VISIBLE);
            }
        });

        membershipText = root.findViewById(R.id.registerMembership);
        membershipText.setEnabled(false);
        membershipText.setFocusable(false);
        policyHolderText = root.findViewById(R.id.registerCompany);
        policyHolderText.setEnabled(false);
        policyHolderText.setFocusable(false);
        policyText = root.findViewById(R.id.registerPolicy);
        policyText.setEnabled(false);
        policyText.setFocusable(false);
        dateOfBirthText = root.findViewById(R.id.registerBirthday);
        dateOfBirthText.setOnClickListener(v -> {

            new DatePickerDialog(getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });


        myCalendar = Calendar.getInstance();

        date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "MMMM d, yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            dateOfBirth = sdf.format(myCalendar.getTime());
            dateOfBirthText.setText(sdf.format(myCalendar.getTime()));
        };

        getUser();
    }

    public boolean verify(){
        boolean valid = true;


        if (fullName.isEmpty() || fullName.equals("")){
            nameText.setError("Enter your Name");
            valid = false;
        }

        if (phone.isEmpty() || phone.equals("")){
            phoneText.setError("Enter your Phone");
            valid = false;
        } else if(phone.length() < 11){
            phoneText.setError("Enter a valid phone number");
            valid = false;
        }

        if (dateOfBirth.isEmpty() || dateOfBirth.equals("")){
            dateOfBirthText.setError("Enter your Date Of Birth");
            valid = false;
        }

        if (gender.isEmpty() || gender.equals("")){
            Toast.makeText(getContext(), "Enter your gender", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private void updateProfile() {

        phone = phoneText.getText().toString().trim();
        fullName = nameText.getText().toString().trim();

        if(!verify()){
            return;
        }
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("fullName", fullName);
        updatedUser.put("phone", phone);
        updatedUser.put("city", city);
        updatedUser.put("gender", gender);
        updatedUser.put("dateOfBirth", dateOfBirth);
        updatedUser.put("isPregnant", isPregnant);
        updatedUser.put("isChronicDiseases", isChronicDiseases);
        updatedUser.put("bloodType", bloodType);
        updatedUser.put("chronicDiseases", chronicDiseases);

        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .update(updatedUser)
                .addOnSuccessListener(aVoid ->
                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                ).addOnFailureListener(e ->
                    Toast.makeText(getContext(), "Something wrong , try again later", Toast.LENGTH_SHORT).show()

        );
    }

    private void getUser(){
        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    user.setUserId(documentSnapshot.getId());
                    showUserDetails();

                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Something wrong , try again later", Toast.LENGTH_SHORT).show()
                );
    }

    private void showUserDetails() {
        nameText.setText(user.getFullName());
        emailText.setText(user.getEmail());
        phoneText.setText(user.getPhone());
        citySpinner.setSelection(cities.indexOf(user.getCity()));
        bloodSpinner.setSelection(bloodTypes.indexOf(user.getBloodType()));
        if (user.getGender().equals("Male")){
            maleBtn.setSelected(true);
            maleBtn.setChecked(true);
        }else if (user.getGender().equals("Female")){
            femaleBtn.setSelected(true);
            femaleBtn.setChecked(true);
        }
        pregnancyCheckBox.setChecked(user.getIsPregnant());
        chronicDiseasesCheckBox.setChecked(user.getIsChronicDiseases());
        dateOfBirthText.setText(user.getDateOfBirth());
        membershipText.setText(user.getMembershipNumber());
        policyHolderText.setText(user.getPolicyHolder());
        policyText.setText(user.getPolicyNumber());

        boolean[] selected = new boolean[chronicDiseases.size()];
        for (int i = 0; i < chronicDiseases.size(); i++) {
            for (int j = 0; j < selectedChronicDiseases.size(); j++) {
                if (chronicDiseases.get(i).equals(selectedChronicDiseases.get(j))){
                    chronicDiseasesSpinner.onClick(null, i, true);
                    selected[i] = true;
                }
            }
        }
        chronicDiseasesSpinner.onCancel(null);

    }


}