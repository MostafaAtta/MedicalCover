package com.atta.medicalcover.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.atta.medicalcover.R;
import com.atta.medicalcover.MainActivity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginFragment extends Fragment implements View.OnClickListener {

    View root;

    Button loginBtn;

    TextInputEditText emailText, passwordText;

    String email, password;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private static final String TAG = "LoginFragment";

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_login, container, false);

        emailText = root.findViewById(R.id.loginEmail);
        passwordText = root.findViewById(R.id.loginPass);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginBtn = root.findViewById(R.id.button_login);
        loginBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Toast.makeText(getContext(), currentUser.getEmail(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view == loginBtn){
            email = emailText.getText().toString().trim();
            password = passwordText.getText().toString().trim();

            if(!verify()){
                return;
            }
            checkUser();

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

    private void checkUser(){
        db.collection("User").whereEqualTo("email", email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){

                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                user = documentSnapshot.toObject(User.class);
                            }

                            login();
                        }else {
                            Toast.makeText(getContext(), "User not found, Please Register", Toast.LENGTH_SHORT).show();
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

    public void login(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();

                            SessionManager.getInstance(getContext()).login(user);
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            getActivity().startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            // ...
                        }

                        // ...
                    }
                });

    }
}