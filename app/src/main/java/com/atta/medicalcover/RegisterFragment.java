package com.atta.medicalcover;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    View root;

    Button registerBtn;

    TextInputEditText emailText, passwordText, nameText, phoneText;

    String email, password, fullName, city, phone;

    private FirebaseAuth mAuth;

    private static final String TAG = "RegisterFragment";

    ArrayAdapter<String> adapter;

    Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
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



        return root;
    }


    public boolean verify(){
        boolean valid = true;

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty() || email.equals("")){
            emailText.setError("Enter you Email");
            valid = false;
        }else if (!email.matches(emailPattern)){
            emailText.setError("Enter a valid Email");
            valid = false;
        }

        if(password.isEmpty() || password.equals("")){
            passwordText.setError("Enter you Password");
            valid = false;
        } else if(password.length() < 6 || password.length() >10){
            passwordText.setError("password must be between 6 and 10 alphanumeric characters");
            valid = false;
        }

        if (fullName.isEmpty() || fullName.equals("")){
            nameText.setError("Enter you Name");
            valid = false;
        }

        if (phone.isEmpty() || phone.equals("")){
            phoneText.setError("Enter you Phone");
            valid = false;
        } else if(phone.length() < 11){
            phoneText.setError("Enter a valid phone number");
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

            if(!verify()){
                return;
            }
            register();
        }
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
}