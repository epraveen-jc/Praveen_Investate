package com.example.praveen_investate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.praveen_investate.MainActivity;
import com.example.praveen_investate.R;
import com.example.praveen_investate.database.DatabaseHelper;
import com.example.praveen_investate.utils.ProfileManager;

public class SettingsActivity extends AppCompatActivity {

    private String[] settingsOptions ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(new DatabaseHelper(this).getProfile().getProfileType().toLowerCase().equals("agent") ||new DatabaseHelper(this).getProfile().getProfileType().toLowerCase().equals("owner") ){
            settingsOptions = new String[]{"Logout","My Posts"};
        }else{
            settingsOptions = new String[]{"Logout"};
        }

        ListView listViewSettings = findViewById(R.id.listViewSettings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, settingsOptions);
        listViewSettings.setAdapter(adapter);

        listViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // Logout option
                    logoutUser();
                }
                if(position == 1){
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(SettingsActivity.this, MyPostsActivity.class);
                        startActivity(intent);
                    }, 1500);
                }
            }
        });
    }

    private void logoutUser() {
        // Clear user profile data from SharedPreferences
        ProfileManager profileManager = new ProfileManager(this);
        profileManager.clearProfileData();
        try{
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.clearUserData();
        }catch(Exception e){
            Toast.makeText(this,"database unable to clear ",Toast.LENGTH_SHORT);
        }

        // Navigate back to MainActivity
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
        startActivity(intent);
        finish(); // Close the SettingsActivity
    }
}
