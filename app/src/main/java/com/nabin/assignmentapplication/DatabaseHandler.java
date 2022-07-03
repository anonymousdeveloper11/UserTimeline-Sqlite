package com.nabin.assignmentapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context){
        super(context,Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//create table
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_POST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //drop table if exists
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_USER);
        onCreate(sqLiteDatabase);
    }
   //insert user  to db
    public long insertUser(String fullName, String address, String email, String registerTime, String updateTime, String password){
        //get writable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //id will be inserted automatically as we set AUTOINCREMENT in query
        //inset data
        values.put(Constants.U_FULL_NAME, fullName);
        values.put(Constants.U_ADDRESS, address);
        values.put(Constants.U_EMAIL, email);
        values.put(Constants.U_REGISTER_TIME, registerTime);
        values.put(Constants.U_UPDATE_TIME, updateTime);
        values.put(Constants.U_PASS, password);


        //inset row ,it will return user id of saved data
        long id = db.insert(Constants.TABLE_USER, null, values);
        //close db connection
        db.close();
        //return id of inserted record
        return id;
    }

    //check register User when login
    public boolean checkUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                "id"
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = Constants.U_EMAIL + " = ?" + " AND " + Constants.U_PASS + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions
        //fetch record
        Cursor cursor = db.query(Constants.TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    //update existing user details to db
    public void updateUserDetails(String id,String fullName, String address, String email, String updateTime
                            ){
        //get writable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //id will be inserted automatically as we set AUTOINCREMENT in query
        //inset data
        values.put(Constants.U_FULL_NAME, fullName);
        values.put(Constants.U_ADDRESS, address);
        values.put(Constants.U_EMAIL, email);
        values.put(Constants.U_UPDATE_TIME, updateTime);


        //inset row ,it will return record id of saved data
        db.update(Constants.TABLE_USER, values, Constants.U_ID +" = ?", new String[]{id});
        //close db connection
        db.close();
        //return id of inserted user record

    }

    public Cursor getUserDetails(String email){

        //read the data
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cos = db.query(Constants.TABLE_USER,null,"email" + "=?",new String[]{email},null,null,null);

        return  cos;
    }
    //get all data
    //get all data
    public ArrayList<UserModel> getAllUsers(String orderBy){
        //orderBy query will allow to start data eg, newest/oldest first, name ascending/descending order
        //it will return list or records since we have used return type ArrayList<ModelRecord>

        ArrayList<UserModel> userList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + Constants.TABLE_USER + " ORDER BY " + orderBy;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        //looping through all records and add to list
        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") UserModel user = new UserModel(
                        ""+cursor.getInt(cursor.getColumnIndex(Constants.U_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.U_FULL_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.U_ADDRESS)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.U_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.U_REGISTER_TIME)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.U_UPDATE_TIME))


                );
                //add records to list
                userList.add(user);
            }while (cursor.moveToNext());
        }
        //close db connection
        db.close();
        //return the list
        return userList;
    }
    public void deleteUser(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_USER, Constants.U_ID + " = ?", new String[] {id});
        db.close();
    }

    //inset user post timeline

    public long insertUserPost(String name, String email, String text, String image, String postTime){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.P_NAME, name);
        contentValues.put(Constants.P_EMAIL, email);
        contentValues.put(Constants.P_TEXT, text);
        contentValues.put(Constants.P_IMAGE, image);
        contentValues.put(Constants.P_TIME, postTime);

        long id = db.insert(Constants.TABLE_POST, null, contentValues);
        db.close();
        return id;
    }
}
