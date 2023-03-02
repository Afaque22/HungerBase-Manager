package com.example.hungerbasemanager.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hungerbasemanager.Home;
import com.example.hungerbasemanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogInFragment extends Fragment {

    TextInputEditText etLogEmail, etLogPswrd;
    Button btnLogIn;
    ProgressBar logProgressBar;
    FirebaseAuth firebaseAuth;

    public LogInFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        etLogEmail = view.findViewById(R.id.etLogEmail);
        etLogPswrd = view.findViewById(R.id.etLogPswrd);
        btnLogIn = view.findViewById(R.id.loginBtn);
        logProgressBar = view.findViewById(R.id.log_progressbar);

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etLogEmail.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter Your Complete Info", Toast.LENGTH_SHORT).show();

                } else if (etLogPswrd.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter Your Complete Info", Toast.LENGTH_SHORT).show();
                } else {
                    logIn();
                }
            }
        });

        onStart();

        return view;
    }

    public void logIn(){
        logProgressBar.setVisibility(View.VISIBLE);
        String email = etLogEmail.getText().toString();
        String password = etLogPswrd.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    logProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Log In Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), Home.class);
                    startActivity(intent);
                    etLogEmail.setText("");
                    etLogPswrd.setText("");
                }else {
                    logProgressBar.setVisibility(View.INVISIBLE);
                    etLogEmail.setText("");
                    etLogPswrd.setText("");
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();



                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getContext(), Home.class));
        }
    }

}
