package com.nabin.assignmentapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nabin.assignmentapplication.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    String name, address, email, password;
    private DatabaseHandler dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHandler(this);
        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        
        name = binding.etFullName.getText().toString().trim();
        address = binding.etAddress.getText().toString().trim();
        email = binding.etEmail.getText().toString().trim();
        password  = binding.etPass.getText().toString().trim();
        
        if(name.isEmpty()){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
        }else if(address.isEmpty()){
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        }else {
            saveToDb();
        }
    }

    private void saveToDb() {
        String timestamp = ""+System.currentTimeMillis();

        long id = dbHelper.insertUser(
                ""+name,
                ""+address,
                ""+email,
                ""+timestamp,
                "",
                ""+password
        );
        Toast.makeText(this, "User Saved  Successfully"+id, Toast.LENGTH_SHORT).show();
    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    startActivity(intent);
    }
}