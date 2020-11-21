package com.atta.medicalcover;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements View.OnClickListener {

    View root;

    Button loginBtn;

    TextInputEditText emailText, passwordText;

    String email, password;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_login, container, false);

        loginBtn = root.findViewById(R.id.button_login);
        loginBtn.setOnClickListener(this);

        emailText = root.findViewById(R.id.loginEmail);
        passwordText = root.findViewById(R.id.loginPass);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == loginBtn){
            email = emailText.getText().toString().trim();
            password = passwordText.getText().toString().trim();

            if(!verify()){
                return;
            }

            Intent intent = new Intent(getContext(), MainActivity.class);
            getActivity().startActivity(intent);
        }
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
        } else if(password.isEmpty() || password.equals("")){
            passwordText.setError("Enter you Password");
            valid = false;
        } else if(password.length() < 6 || password.length() >10){
            passwordText.setError("password must be between 6 and 10 alphanumeric characters");
            valid = false;
        }

        return valid;
    }
}