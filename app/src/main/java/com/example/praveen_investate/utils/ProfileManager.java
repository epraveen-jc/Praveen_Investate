package com.example.praveen_investate.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class ProfileManager {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAME = "profileName";
    private static final String KEY_EMAIL = "profileEmail";
    private static final String KEY_PROFILE_TYPE = "profileType";
    private static final String KEY_PHONE = "profilePhoneNumber";
    private static final String KEY_IMAGE = "profileImage";
    private static final String KEY_STATE = "profileState";
    private static final String KEY_DISTRICT = "profileDistrict";
    private static final String KEY_COLONY = "profileStreetOrColony";

    private SharedPreferences sharedPreferences;

    public ProfileManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply();
    }

    public String getName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getProfileType() {
        return sharedPreferences.getString(KEY_PROFILE_TYPE, "");
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString(KEY_PHONE, "");
    }

    public String getProfileImage() {
        return sharedPreferences.getString(KEY_IMAGE, "");
    }

    public String getState() {
        return sharedPreferences.getString(KEY_STATE, "");
    }

    public String getDistrict() {
        return sharedPreferences.getString(KEY_DISTRICT, "");
    }

    public String getStreetOrColony() {
        return sharedPreferences.getString(KEY_COLONY, "");
    }

    public void clearProfileData() {
        sharedPreferences.edit().clear().apply();
    }
}
