package com.nabin.assignmentapplication;

public class Constants {
    //db Name
    public static final String DB_NAME = "USER_DB";
    //db Version
    public static final int DB_VERSION = 1;
    //table name
    public static final String TABLE_USER = "USER_TABLE";
    public static final String TABLE_POST ="POST_TABLE";

    public static final String U_ID = "ID";
    public static final String U_FULL_NAME = "FULL_NAME";
    public static final String U_ADDRESS = "ADDRESS";
    public static final String U_EMAIL = "EMAIL";
    public static final String U_PASS = "PASSWORD";
    public static final String U_REGISTER_TIME = "REGISTER_TIME";
    public static final String U_UPDATE_TIME = "UPDATE_TIME";

    public static final String P_ID ="POST_ID";
    public static final String P_NAME="POST_NAME";
    public static final String P_EMAIL ="POST_EMAIL";
    public static final String P_TEXT ="POST_TEXT";
    public static final String P_IMAGE ="POST_IMAGE";
    public static final String P_TIME ="POST_TIME";


    public static final String KEY_PREFERENCE_NAME ="ASSIGNMENT_PROJECT";
    public static final String KEY_IS_SIGNED_IN ="isLogin";
    public static final String KEY_EMAIL ="EMAIL";



    public static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +  U_FULL_NAME + " TEXT,"
            + U_ADDRESS + " TEXT,"
            + U_EMAIL + " TEXT,"
            + U_PASS + " TEXT,"
            + U_REGISTER_TIME + " TEXT,"
            + U_UPDATE_TIME + " TEXT"
            + ")";

    public static final String CREATE_TABLE_POST ="CREATE TABLE " + TABLE_POST + "("
            +P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +P_EMAIL + " TEXT,"
            +P_TEXT + " TEXT,"
            +P_IMAGE + "TEXT"
            +")";



}
