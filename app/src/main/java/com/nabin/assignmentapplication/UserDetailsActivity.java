package com.nabin.assignmentapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import com.nabin.assignmentapplication.databinding.ActivityUserDetailsBinding;

import java.util.Calendar;
import java.util.Locale;

public class UserDetailsActivity extends AppCompatActivity {
    private boolean isEditMode = false;

    private ActivityUserDetailsBinding binding;
    private DatabaseHandler handler;
    private PreferenceManager preferenceManager;
    String email, uid,name, address, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handler = new DatabaseHandler(this);
        preferenceManager = new PreferenceManager(this);
        email = preferenceManager.getString(Constants.KEY_EMAIL);


        binding.nameEt.setEnabled(false);
        binding.addressEt.setEnabled(false);
        binding.emailEt.setEnabled(false);
        binding.btnUpdate.setVisibility(View.GONE);




        getUserDetails();

//        if (!email.isEmpty()) {
//            Cursor cursor = handler.getUserDetails(email);
//            cursor.moveToFirst();
//
//            @SuppressLint("Range") String fullName = cursor.getString(cursor.getColumnIndex(Constants.U_FULL_NAME));
//            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(Constants.U_ADDRESS));
//            @SuppressLint("Range") String registerDate = cursor.getString(cursor.getColumnIndex(Constants.U_REGISTER_TIME));
//            @SuppressLint("Range") String updateDate = cursor.getString(cursor.getColumnIndex(Constants.U_UPDATE_TIME));
//            if (!cursor.isClosed()) {
//                cursor.close();
//            }
//
//            //convert time
//            Calendar calendar = Calendar.getInstance(Locale.getDefault());
//            calendar.setTimeInMillis(Long.parseLong(registerDate));
//            String timeRegister = "" + DateFormat.format("dd/MM/yyy hh:mm:aa", calendar);
//
//            //set Text
//            binding.nameEt.setText(fullName);
//            binding.addressEt.setText(address);
//            binding.emailEt.setText(email);
//            binding.registerDateEt.setText(timeRegister);
//            binding.registerDateEt.setEnabled(false);
//            binding.editTv.setVisibility(View.VISIBLE);
//
//            if(updateDate.equals("")){
//                binding.updateDateEt.setVisibility(View.GONE);
//                binding.tvUpdate.setVisibility(View.GONE);
//            }else {
//                Calendar calendarUpdateTime = Calendar.getInstance(Locale.getDefault());
//                calendarUpdateTime.setTimeInMillis(Long.parseLong(updateDate));
//                String timeUpdate = "" + DateFormat.format("dd/MM/yyy hh:mm:aa", calendarUpdateTime);
//         binding.updateDateEt.setVisibility(View.VISIBLE);
//                binding.tvUpdate.setVisibility(View.VISIBLE);
//
//                binding.updateDateEt.setText(timeUpdate);
//            }
//        }
        binding.editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditMode= true;
                binding.nameEt.setEnabled(true);
                binding.addressEt.setEnabled(true);
                binding.emailEt.setEnabled(true);
                binding.registerDateEt.setEnabled(false);
                binding.btnUpdate.setVisibility(View.VISIBLE);
                binding.editTv.setVisibility(View.GONE);


            }
        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = binding.nameEt.getText().toString().trim();
                address = binding.addressEt.getText().toString().trim();
                mail = binding.emailEt.getText().toString().trim();
                String time = ""+System.currentTimeMillis();

                handler.updateUserDetails(""+uid,
                        ""+name,
                        ""+address,
                        ""+mail,
                        ""+time);

                binding.btnUpdate.setVisibility(View.GONE);

                    Toast.makeText(UserDetailsActivity.this, ""+name+" profile updated.", Toast.LENGTH_SHORT).show();
                binding.editTv.setVisibility(View.VISIBLE);

                binding.nameEt.setEnabled(false);
                binding.addressEt.setEnabled(false);
                binding.emailEt.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);


            }
        });



        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.ibLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                preferenceManager.clearPreference();
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }





    private void getUserDetails() {
       // if (!email.isEmpty()) {
            Cursor cursor = handler.getUserDetails(email);
            cursor.moveToFirst();

            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(Constants.U_ID));
            @SuppressLint("Range") String fullName = cursor.getString(cursor.getColumnIndex(Constants.U_FULL_NAME));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(Constants.U_ADDRESS));
            @SuppressLint("Range") String registerDate = cursor.getString(cursor.getColumnIndex(Constants.U_REGISTER_TIME));
            @SuppressLint("Range") String updateDate = cursor.getString(cursor.getColumnIndex(Constants.U_UPDATE_TIME));
            if (!cursor.isClosed()) {
                cursor.close();
            }

            //convert time
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(Long.parseLong(registerDate));
            String timeRegister = "" + DateFormat.format("yyy-MM-dd hh:mm:aa", calendar);

            uid = id;
            //set Text
            binding.nameEt.setText(fullName);
            binding.addressEt.setText(address);
            binding.emailEt.setText(email);
            binding.registerDateEt.setText(timeRegister);
            binding.registerDateEt.setEnabled(false);
            binding.editTv.setVisibility(View.VISIBLE);
            binding.updateDateEt.setEnabled(false);

            if(updateDate.equals("")){
                binding.updateDateEt.setVisibility(View.GONE);
                binding.tvUpdate.setVisibility(View.GONE);
            }else {
                Calendar calendarUpdateTime = Calendar.getInstance(Locale.getDefault());
                calendarUpdateTime.setTimeInMillis(Long.parseLong(updateDate));
                String timeUpdate = "" + DateFormat.format("yyy/MM/dd hh:mm:aa", calendarUpdateTime);
                binding.updateDateEt.setVisibility(View.VISIBLE);
                binding.tvUpdate.setVisibility(View.VISIBLE);

                binding.updateDateEt.setText(timeUpdate);
            }
        }

   // }
}