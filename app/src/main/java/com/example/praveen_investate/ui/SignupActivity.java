package com.example.praveen_investate.ui;

import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.praveen_investate.encryption_decryption.PsychoCipher;
import com.example.praveen_investate.model.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SignupActivity extends AppCompatActivity {

    private EditText nameEditText, passwordEditText, emailEditText, phoneEditText, colonyEditText;
    private Spinner profileTypeSpinner, stateSpinner, districtSpinner;
    private TextView tv;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ImageView blurredImageView = findViewById(R.id.blurredImageView);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // Get the hour in 24-hour format
        if (hour >= 19 || hour < 7) {

            switch(new Random().nextInt(9)) {
                case 1 : Glide.with(this)
                        .load(R.drawable.loginback) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break;
                case 2 : Glide.with(this)
                        .load(R.drawable.back11) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 3 : Glide.with(this)
                        .load(R.drawable.back4) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 4 : Glide.with(this)
                        .load(R.drawable.back4) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 5 : Glide.with(this)
                        .load(R.drawable.back5) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 6 : Glide.with(this)
                        .load(R.drawable.back11) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 7 : Glide.with(this)
                        .load(R.drawable.back4) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 8 : Glide.with(this)
                        .load(R.drawable.back11) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break;
                default: Glide.with(this)
                        .load(R.drawable.back11) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break;
            }
        }else{

            switch(new Random().nextInt(8)) {

                case 1 : Glide.with(this)
                        .load(R.drawable.back4) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break;
                case 2 : Glide.with(this)
                        .load(R.drawable.back4) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 3 : Glide.with(this)
                        .load(R.drawable.back4) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break; case 4 : Glide.with(this)
                        .load(R.drawable.back11) // Replace with your image resource
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
                        .load(R.drawable.back11) // Replace with your image resource
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 1))) // 25 is the radius, 3 is the sampling
                        .into(blurredImageView);
                    break;
            }
        }
        nameEditText = findViewById(R.id.nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        colonyEditText = findViewById(R.id.colonyEditText);
        profileTypeSpinner = findViewById(R.id.profileTypeSpinner);
        stateSpinner = findViewById(R.id.stateSpinner);
        districtSpinner = findViewById(R.id.districtSpinner);
        tv = findViewById(R.id.signal);

        setupSpinners();

        requestQueue = Volley.newRequestQueue(this);
    }

    private void setupSpinners() {
        // Setup Profile Type Spinner
        ArrayAdapter<CharSequence> profileTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.profile_types, android.R.layout.simple_spinner_item);
        profileTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileTypeSpinner.setAdapter(profileTypeAdapter);

        // Setup State Spinner
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);

        // Setup District Spinner
        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(this,
                R.array.districts, android.R.layout.simple_spinner_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);
    }

    public void onRegisterClick(View view) {
        String name = nameEditText.getText().toString().trim();
        String password = new PsychoCipher(passwordEditText.getText().toString().trim()).getEncryptedString();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String colony = colonyEditText.getText().toString().trim();
        String profileType = profileTypeSpinner.getSelectedItem().toString();
        String state = stateSpinner.getSelectedItem().toString();
        String district = districtSpinner.getSelectedItem().toString();

        // Create the Profile object
        Profile profile = new Profile();
        profile.setName(name);
        profile.setPassword(password);
        profile.setEmail(email);
        profile.setPhoneNumber(phone);
        profile.setStreetOrColony(colony);
        profile.setProfileType(profileType);
        profile.setState(state);
        profile.setDistrict(district);

        // Convert Profile to JSON object
        JSONObject profileJson = new JSONObject();
        try {
            profileJson.put("name", name);
            profileJson.put("password", password);
            profileJson.put("email", email);
            profileJson.put("phoneNumber", phone);
            profileJson.put("streetOrColony", colony);
            profileJson.put("profileType", profileType);
            profileJson.put("state", state);
            profileJson.put("district", district);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Perform API call using Volley
        String url = "http://10.0.2.2:1010/api/auth/register";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                profileJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Log the raw response
                        Log.d("SignupResponse", response.toString());
                        try {
                            if (response.has("message")) {
                                String message = response.getString("message");
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else if (response.has("error")) {
                                String error = response.getString("error");
                                Toast.makeText(SignupActivity.this, error, Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle unexpected response format
                                Toast.makeText(SignupActivity.this, "Unexpected response format.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("SignupError", "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(SignupActivity.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log the error details
                        error.printStackTrace();
                        Log.e("SignupError", "Error: " + error.getMessage());
                        if (error.networkResponse != null) {
                            // Log the response code and data if available
                            Log.e("SignupError", "Error Response Code: " + error.networkResponse.statusCode);
                            Log.e("SignupError", "Error Response Data: " + new String(error.networkResponse.data));
                        }
                        tv.setText(error.getMessage());
                        Toast.makeText(SignupActivity.this, "Registration failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
