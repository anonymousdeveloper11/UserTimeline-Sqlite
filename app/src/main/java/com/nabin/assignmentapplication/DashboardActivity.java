package com.nabin.assignmentapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nabin.assignmentapplication.databinding.ActivityDashboardBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    //permission Constants
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=101;
    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE=102;
    private static final int IMAGE_PICK_GALLERY_CODE=103;
    //array of permissions
    private String[] cameraPermissions;//camera and storage
    private String[] storagePermission;//only storage

    private Uri imageUri;
    private PreferenceManager preferenceManager;
    private DatabaseHandler databaseHandler;
    String name, email, text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        databaseHandler = new DatabaseHandler(this);
        email = preferenceManager.getString(Constants.KEY_EMAIL);


        Cursor cursor = databaseHandler.getUserDetails(email);
        cursor.moveToFirst();

        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Constants.U_FULL_NAME));
        if (!cursor.isClosed()) {
            cursor.close();
        }
        //init  permissions of array
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        binding.ibUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                startActivity(intent);
            }
        });
        
        binding.ibImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timestamp = "" + System.currentTimeMillis();
                text = binding.etTimeline.getText().toString().trim();

                long id = databaseHandler.insertUserPost(name,email,""+text,""+imageUri, ""+timestamp);
                Toast.makeText(DashboardActivity.this, "Successfully posted"+id, Toast.LENGTH_SHORT).show();

                binding.etTimeline.setText("");
                binding.ibImage.setVisibility(View.GONE);
            }
        });
    }

    private void pickImage() {
        //options to display dialogs
        String[] options ={"Camera","Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //title
        builder.setTitle("Pick Image From");
        //set item options
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle click
                if(which==0){
                    //camera clicked
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else {
                        //permission already granted
                        pickFromCamera();
                    }
                }
                else if(which ==1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else {
                        pickFromGallery();
                    }
                }
            }

        });
        //show dialog
        builder.create().show();

    }

    private void pickFromGallery() {
        //intent to pick From Gallery,the image will be  returned in onActivityResult method
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");//we want only images
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Image_Title");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image_description");
        //put image
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //intent to open camera for image
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private boolean checkStoragePermission(){
        //check storage permission is enable or not
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //request storage permission
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //results of permission
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                //if allowed return true otherwise false
                if(grantResults.length>0){
                    boolean cameraAccepted =grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        //both permission allowed
                        pickFromCamera();

                    } else {
                        Toast.makeText(this, "Camera and Storage permission are required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        //permission allowed
                        pickFromGallery();
                    }else {
                        Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // if (requestCode == IMAGE_PICK_GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Uri selectedImage = data.getData();
                        //  try {
                        imageUri = selectedImage;
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivi.getContentResolver(), selectedImage);
//                        carImage.setImageBitmap(bitmap);
                       binding.ibImage.setImageURI(selectedImage);
                        copyFileOrDirectory("" + imageUri.getPath(), "" + getDir("SQLiteRecordImages", MODE_PRIVATE));
//        }
                        //   } catch (Exception e) {
//                        Log.i("TAG", "Some exception " + e);
//                    }
                        // Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
            // }
        }

   // }
   private void copyFileOrDirectory(String srcDir, String desDir){
       //create fikdir in specified directory
       try{
           File src = new File(srcDir);
           File des = new File(desDir, src.getName());
           if(src.isDirectory()){
               String[] files = src.list();
               int fileLength = files.length;
               for(String file : files){
                   String src1 = new File(src, file).getPath();
                   String dst1 = des.getPath();
                   copyFileOrDirectory(src1, dst1);
               }
           }
           else {
               copyFile(src, des);
           }
       }catch (Exception e){
           Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
       }
   }

    private void copyFile(File srcDir, File desDir) throws IOException {

        if(!desDir.getParentFile().exists()){
            desDir.mkdirs();//create if not exists
        }
        if(!desDir.exists()){
            desDir.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try{

            source = new FileInputStream(srcDir).getChannel();
            destination = new FileOutputStream(desDir).getChannel();
            destination.transferFrom(source,0,source.size());
            imageUri = Uri.parse(desDir.getPath());//uri of saved image
            Log.d("ImagePath","copyFile: " +imageUri);
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            {
                //close resources
                if(source!=null){
                    source.close();
                }
                if (destination!=null){
                    destination.close();
                }
            }
        }
    }
}