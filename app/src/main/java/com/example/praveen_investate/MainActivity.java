package com.example.praveen_investate;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.praveen_investate.ui.HomeActivity;
import com.example.praveen_investate.ui.LoginActivity;
import com.example.praveen_investate.ui.ToastMaker;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private TelephonyManager telephonyManager;
    private String imei="";
    private String uniqueId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        finish(); // Close MainActivity after redirecting
    }

}
