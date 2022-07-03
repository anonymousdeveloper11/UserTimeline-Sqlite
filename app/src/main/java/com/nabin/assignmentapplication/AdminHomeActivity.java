package com.nabin.assignmentapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nabin.assignmentapplication.databinding.ActivityAdminHomeBinding;

public class AdminHomeActivity extends AppCompatActivity {

    private ActivityAdminHomeBinding binding;
    private DatabaseHandler databaseHandler;
    private String orderBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHandler = new DatabaseHandler(this);

        binding.ibLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminHomeActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        UserAdapter userAdapter = new UserAdapter(this, databaseHandler.getAllUsers(orderBy));
        binding.rvUser.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();



    }
}