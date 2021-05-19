package com.atta.medicalcover.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.atta.medicalcover.ui.MainActivity;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.atta.medicalcover.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    View root;

    Button registerBtn;

    TextInputEditText emailText, passwordText, nameText, phoneText,
            membershipText, policyHolderText, policyText;

    EditText dateOfBirthText;

    String email, password, fullName, city, phone, membershipNumber, policyHolder, policyNumber,
            dateOfBirth = "", gender = "";

    RadioGroup genderRadio;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String TAG = "RegisterFragment";

    ArrayAdapter<String> adapter;

    Spinner spinner;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        initiateViews();

        return root;
    }

    private void initiateViews() {
        db = FirebaseFirestore.getInstance();

        spinner = root.findViewById(R.id.location_spinner);

        registerBtn = root.findViewById(R.id.button_register);
        registerBtn.setOnClickListener(this);

        emailText = root.findViewById(R.id.registerEmail);
        passwordText = root.findViewById(R.id.registerPass);
        nameText = root.findViewById(R.id.registerName);
        phoneText = root.findViewById(R.id.registerPhone);

        ArrayList<String> cities = new ArrayList<>();
        cities.add("Cairo");
        cities.add("Alex");
        cities.add("Giza");
        cities.add("Luxor");
        cities.add("Tanta");

        adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                cities);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                city = cities.get(0);
            }
        });

        genderRadio = root.findViewById(R.id.genderRadio);
        genderRadio.setOnCheckedChangeListener((radioGroup, i) -> {

            if (i == R.id.maleBtn) {
                gender = "Male";
            }else if (i == R.id.femaleBtn){
                gender = "Female";
            }
        });

        membershipText = root.findViewById(R.id.registerMembership);
        policyHolderText = root.findViewById(R.id.registerCompany);
        policyText = root.findViewById(R.id.registerPolicy);
        dateOfBirthText = root.findViewById(R.id.registerBirthday);
        dateOfBirthText.setOnClickListener(this);


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
    }


    public boolean verify(){
        boolean valid = true;

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty() || email.equals("")){
            emailText.setError("Enter your Email");
            valid = false;
        }else if (!email.matches(emailPattern)){
            emailText.setError("Enter a valid Email");
            valid = false;
        }

        if(password.isEmpty() || password.equals("")){
            passwordText.setError("Enter your Password");
            valid = false;
        } else if(password.length() < 6 || password.length() >10){
            passwordText.setError("password must be between 6 and 10 alphanumeric characters");
            valid = false;
        }

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

        if (membershipNumber.isEmpty() || membershipNumber.equals("")){
            membershipText.setError("Enter your Membership Number");
            valid = false;
        }

        if (policyHolder.isEmpty() || policyHolder.equals("")){
            policyHolderText.setError("Enter your Company Name");
            valid = false;
        }

        if (policyNumber.isEmpty() || policyNumber.equals("")){
            policyText.setError("Enter your Policy Number");
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

    @Override
    public void onClick(View view) {
        if (view == registerBtn){
            email = emailText.getText().toString().trim();
            password = passwordText.getText().toString().trim();
            phone = phoneText.getText().toString().trim();
            fullName = nameText.getText().toString().trim();
            membershipNumber = membershipText.getText().toString().trim();
            policyHolder = policyHolderText.getText().toString().trim();
            policyNumber = policyText.getText().toString().trim();

            if(!verify()){
                return;
            }
            checkUser();
        }else if (view == dateOfBirthText){

            new DatePickerDialog(getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        }
    }

    private void checkUser(){
        db.collection("Users").whereEqualTo("email", email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            register();
                        }else {
                            Toast.makeText(getContext(), "User already exist, Please Login", Toast.LENGTH_SHORT).show();
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

    private void register(){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            createUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    private void createUser(){
        User mUser = new User(fullName, email, phone, city, membershipNumber, policyHolder, policyNumber);
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("phone", phone);
        user.put("city", city);
        user.put("membershipNumber", membershipNumber);
        user.put("policyHolder", policyHolder);
        user.put("policyNumber", policyNumber);
        user.put("gender", gender);
        user.put("dateOfBirth", dateOfBirth);
        user.put("type", 5);

        db.collection("Users").add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {


                        mUser.setUserId(documentReference.getId());
                        SessionManager.getInstance(getContext()).login(mUser);
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        getActivity().startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}