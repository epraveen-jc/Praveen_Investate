package com.example.praveen_investate.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.praveen_investate.model.Profile;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_profiles.db";
    private static final int DATABASE_VERSION = 1;

    // Table and Column Names
    public static final String TABLE_PROFILE = "profiles";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PROFILE_TYPE = "profile_type";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_PROFILE_IMAGE = "profile_image";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_DISTRICT = "district";
    public static final String COLUMN_STREET_OR_COLONY = "street_or_colony";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PROFILE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PROFILE_TYPE + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_PROFILE_IMAGE + " TEXT, " +
                COLUMN_STATE + " TEXT, " +
                COLUMN_DISTRICT + " TEXT, " +
                COLUMN_STREET_OR_COLONY + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        onCreate(db);
    }

    // Method to insert a profile into the database
    public void insertProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, profile.getName());
        values.put(COLUMN_EMAIL, profile.getEmail());
        values.put(COLUMN_PROFILE_TYPE, profile.getProfileType());
        values.put(COLUMN_PHONE_NUMBER, profile.getPhoneNumber());
        values.put(COLUMN_PROFILE_IMAGE, profile.getProfileImage());
        values.put(COLUMN_STATE, profile.getState());
        values.put(COLUMN_DISTRICT, profile.getDistrict());
        values.put(COLUMN_STREET_OR_COLONY, profile.getStreetOrColony());

        db.insert(TABLE_PROFILE, null, values);
        db.close();
    }

    public void clearUserData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PROFILE);
        db.close();
    }
    // Method to retrieve a profile by name
    // Method to retrieve the first profile in the database
    public Profile getProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROFILE, null, null, null, null, null, null); // No filtering condition

        if (cursor != null && cursor.moveToFirst()) {
            Profile profile = new Profile();

            profile.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            profile.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            profile.setProfileType(cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE_TYPE)));
            profile.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
            profile.setProfileImage(cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE_IMAGE)));
            profile.setState(cursor.getString(cursor.getColumnIndex(COLUMN_STATE)));
            profile.setDistrict(cursor.getString(cursor.getColumnIndex(COLUMN_DISTRICT)));
            profile.setStreetOrColony(cursor.getString(cursor.getColumnIndex(COLUMN_STREET_OR_COLONY)));

            cursor.close();
            return profile;
        }
        return null;
    }

}

