package com.example.praveen_investate.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.praveen_investate.R;
import com.example.praveen_investate.database.DatabaseHelper;
import com.example.praveen_investate.model.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


import jp.wasabeef.glide.transformations.BlurTransformation;

public class LoginActivity extends AppCompatActivity {

    private VideoView videoView;
    private int count = 0;
    private ImageView blurredOverlay;
    private EditText editTextName, editTextPassword;
    private Button buttonLogin;
    private RequestQueue requestQueue;
    private TextView textViewSignUp , resetPwd ;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView blurredImageView = findViewById(R.id.blurredImageView);
        Calendar calendar = Calendar.getInstance();
        resetPwd = findViewById(R.id.resetPasswordBtn);
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // Get the hour in 24-hour format

        // Check if the current time is between 7 PM (19) and 7 AM (7)
        if (hour >= 19 || hour < 7) {
            switch(new Random().nextInt(9)) {
                case 1 : Glide.with(this)
                        .load(R.drawable.loginback) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break;
                case 2 : Glide.with(this)
                        .load(R.drawable.back1) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 3 : Glide.with(this)
                        .load(R.drawable.back12) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 4 : Glide.with(this)
                        .load(R.drawable.back3) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 5 : Glide.with(this)
                        .load(R.drawable.back5) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 6 : Glide.with(this)
                        .load(R.drawable.back8) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 7 : Glide.with(this)
                        .load(R.drawable.back9) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 8 : Glide.with(this)
                        .load(R.drawable.back6) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break;
                default: Glide.with(this)
                        .load(R.drawable.loginback) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break;
            }
        }else{
            switch(new Random().nextInt(8)) {
                case 1 : Glide.with(this)
                        .load(R.drawable.loginback) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break;
                case 2 : Glide.with(this)
                        .load(R.drawable.back2) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 3 : Glide.with(this)
                        .load(R.drawable.back4) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 4 : Glide.with(this)
                        .load(R.drawable.back6) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 5 : Glide.with(this)
                        .load(R.drawable.back7) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 6 : Glide.with(this)
                        .load(R.drawable.back10) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 7 : Glide.with(this)
                        .load(R.drawable.back11) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                   break;
                default: Glide.with(this)
                        .load(R.drawable.loginback) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break;
            }
        }

        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
        databaseHelper = new DatabaseHelper(this);





        // Load the image and apply blur




        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        // Check if user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void loginUser() {


        String name = editTextName.getText().toString();
        String password = editTextPassword.getText().toString();

        JSONObject profile = new JSONObject();
        try {
            profile.put("name", name);
            profile.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(count > 3){
            resetPwd.setVisibility(View.VISIBLE);
            resetPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                    // Assuming agentName is the username
                    startActivity(intent);
                }
            });
        }

        String url = "http://10.0.2.2:1010/api/auth/login"; // Your API endpoint
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                profile,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                        // Fetch the profile data after successful login
                        fetchUserProfile(name);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 401) {
                                count++;
                                // Unauthorized, likely due to invalid credentials
                                Log.e("LoginError", "Invalid credentials. Status code: 401");
                                Toast.makeText(LoginActivity.this, "Login failed. Invalid credentials.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Other server error
                                Log.e("LoginError", "Server error. Status code: " + statusCode);
                                showCustomDialog();
                                Toast.makeText(LoginActivity.this, "Server error: " + statusCode, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Network error or server is down
                            showCustomDialog();
                            Log.e("LoginError", "Server is not reachable or network issue", error);
                            Toast.makeText(LoginActivity.this, "Server is not reachable. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
    private void showCustomDialog() {
        Dialog dialog = new Dialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        dialog.setContentView(dialogView);

        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        Button closeButton = dialogView.findViewById(R.id.dialog_button);

        dialogImage.setImageResource(R.drawable.server);

        // Set up the button to close the dialog
        closeButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }
    private void fetchUserProfile(String name) {
        String profileUrl = "http://10.0.2.2:1010/api/auth/profile/" + name;
        JsonObjectRequest profileRequest = new JsonObjectRequest(
                Request.Method.GET,
                profileUrl,
                null,
                response -> {
                    try {
                        JSONObject profileData = response.getJSONObject("profile");

                        // Populate Profile model
                        Profile profile = new Profile(
                                profileData.getString("name"),
                                "", // Don't store password here
                                profileData.getString("email"),
                                profileData.getString("profileType"),
                                profileData.getString("phoneNumber"),
                                profileData.getString("profileImage"),
                                profileData.getString("state"),
                                profileData.getString("district"),
                                profileData.getString("streetOrColony")
                        );
                        databaseHelper.insertProfile(profile);
                        // Save profile data in SharedPreferences if needed
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("profileName", profile.getName());
                        editor.putString("profileEmail", profile.getEmail());
                        editor.putString("profileType", profile.getProfileType());
                        editor.putString("profilePhoneNumber", profile.getPhoneNumber());
                        editor.putString("profileImage", profile.getProfileImage());
                        editor.putString("profileState", profile.getState());
                        editor.putString("profileDistrict", profile.getDistrict());
                        editor.putString("profileStreetOrColony", profile.getStreetOrColony());
                        editor.apply();
                        String profileImageUrl = profile.getProfileImage();
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            downloadProfileImage(profileImageUrl, profile.getName());
                        }

                        // Proceed to HomeActivity
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error parsing profile data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(LoginActivity.this, "Failed to fetch profile data.", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(profileRequest);
    }
    private void downloadProfileImage(String imageUrl, String userName) {
        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    File imageDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ProfileImages");
                    if (!imageDirectory.exists()) imageDirectory.mkdirs();

                    File imageFile = new File(imageDirectory, userName + "_profile.jpg");

                    try (InputStream inputStream = connection.getInputStream();
                         FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Profile image downloaded", Toast.LENGTH_SHORT).show());
                    }
                }
            } catch (IOException e) {
                Toast.makeText(this,"IOEXception ",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }).start();
    }
}
