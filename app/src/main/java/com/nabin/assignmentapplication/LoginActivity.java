package com.nabin.assignmentapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nabin.assignmentapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    String email, pass;
    private DatabaseHandler db;
    private PreferenceManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHandler(this);
        pref = new PreferenceManager(this);

        if(pref.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        }
        binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginData();
            }
        });
    }

    private void checkLoginData() {
        
        email = binding.emailEt.getText().toString().trim();
        pass = binding.passEt.getText().toString().trim();
        if(binding.emailEt.getText().toString().trim().equals("nabin") && binding.passEt.getText().toString().trim().equals("nabin")) {
Intent intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
startActivity(intent);
finish();

        }else if(email.isEmpty()){
            Toast.makeText(this, "Please enter your valid email", Toast.LENGTH_SHORT).show();
        }else if(pass.isEmpty()){
            Toast.makeText(this, "Please enter your valid password", Toast.LENGTH_SHORT).show();
        }else {
            login();
        }
               
    }

    private void login() {

       if( db.checkUser(email,pass)){
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            pref.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
            pref.putString(Constants.KEY_EMAIL, email);
            startActivity(intent);
            finish();
        }else {
           Toast.makeText(this, "Please Enter valid email and password", Toast.LENGTH_SHORT).show();
       }
    }
}