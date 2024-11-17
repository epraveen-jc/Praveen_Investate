package com.example.praveen_investate.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.praveen_investate.MainActivity;
import com.example.praveen_investate.R;
import com.example.praveen_investate.database.DatabaseHelper;
import com.example.praveen_investate.model.Post;
import com.example.praveen_investate.model.Profile;
import com.example.praveen_investate.utils.ProfileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameText, emailText, phoneText, stateText, districtText, streetText , profiletypetext;
    private EditText editName, editEmail, editPhone, street;
    private Spinner stateSpinner, districtSpinner , profileTypeSpinner;
    private ImageView profileImage;
    private Button editButton;

    String oldName ="";
    private String userName = "no user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        try{
            userName = new DatabaseHelper(this).getProfile().getName();  // This should be the logged-in user's name.
            oldName = userName;
        } catch (Exception e) {

        }
        // Initialize UI components
        nameText = findViewById(R.id.profile_name);
        profileTypeSpinner = findViewById(R.id.profileTypeSpinner);
        profiletypetext = findViewById(R.id.profileTypeTv);
        profileImage = findViewById(R.id.profile_image);
        profileImage.setVisibility(View.VISIBLE);
        emailText = findViewById(R.id.profile_email);
        phoneText = findViewById(R.id.profile_phone);
        stateText = findViewById(R.id.profile_state);
        districtText = findViewById(R.id.profile_district);
        streetText = findViewById(R.id.profile_street);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        stateSpinner = findViewById(R.id.state_spinner);
        districtSpinner = findViewById(R.id.district_spinner);
        street = findViewById(R.id.street);
        profileImage = findViewById(R.id.profile_image);
        editButton = findViewById(R.id.edit_button);

        String imageuri = "";
        try{
            imageuri = new DatabaseHelper(this).getProfile().getProfileImage();
        } catch (Exception e) {

        }
        Glide.with(this)
                .load(imageuri)
                .error(R.drawable.images)
                .into(profileImage);
        // Fetch profile data
        fetchProfileData(userName);

        // Handle edit button click
        editButton.setOnClickListener(v -> {
            toggleEditMode();
        });


    }

    private void fetchProfileData(String userName) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:1010/api/auth/profile/" + userName;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject profileData = response.getJSONObject("profile");
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

                        nameText.setText(profileData.getString("name"));
                        emailText.setText(profileData.getString("email"));
                        phoneText.setText(profileData.getString("phoneNumber"));
                        stateText.setText(profileData.getString("state"));
                        districtText.setText(profileData.getString("district"));
                        streetText.setText(profileData.getString("streetOrColony"));
                        profiletypetext.setText(profileData.getString("profileType"));

                        // Load profile image using Glide
                        Glide.with(this)
                                .load(profileData.getString("profileImage"))
                                .into(profileImage);

                        // Populate Spinners (just example data here)
                        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this,
                                R.array.states, android.R.layout.simple_spinner_item);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        stateSpinner.setAdapter(stateAdapter);

                        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(this,
                                R.array.districts, android.R.layout.simple_spinner_item);
                        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        districtSpinner.setAdapter(districtAdapter);

                        ArrayAdapter<CharSequence> profileTypeAdapter = ArrayAdapter.createFromResource(this,
                                R.array.profile_types, android.R.layout.simple_spinner_item);
                        profileTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        profileTypeSpinner.setAdapter(profileTypeAdapter);

                        // Set the state, district, and street spinner selections based on fetched data
                        stateSpinner.setSelection(getSpinnerIndex(stateSpinner, profileData.getString("state")));
                        districtSpinner.setSelection(getSpinnerIndex(districtSpinner, profileData.getString("district")));
                        profileTypeSpinner.setSelection(getSpinnerIndex(profileTypeSpinner, profileData.getString("profileType")));

                    } catch (Exception e) {

                        Toast.makeText(this, "Error fetching profile", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            showCustomDialog("SERVER PROBLEM or NETWORK ISSUE");

            Toast.makeText(this, "Error fetching profile", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(request);
    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                return i;
            }
        }
        return 0; // Default to the first item if not found
    }

    private void toggleEditMode() {
        boolean isEditable = editName.getVisibility() == View.GONE;

        if (isEditable) {
            // Switch to Edit mode - make fields editable
            nameText.setVisibility(View.GONE);
            emailText.setVisibility(View.GONE);phoneText.setVisibility(View.GONE);stateText.setVisibility(View.GONE);
            districtText.setVisibility(View.GONE);streetText.setVisibility(View.GONE);
            stateSpinner.setVisibility(View.GONE);
           profiletypetext.setVisibility(View.GONE);
            districtText.setVisibility(View.GONE);
            editName.setVisibility(View.VISIBLE);
            editEmail.setVisibility(View.VISIBLE);
            editPhone.setVisibility(View.VISIBLE);
            stateSpinner.setVisibility(View.VISIBLE);
            profileTypeSpinner.setVisibility(View.VISIBLE);
            districtSpinner.setVisibility(View.VISIBLE);
            street.setVisibility(View.VISIBLE);

            editName.setText(nameText.getText().toString());
            editEmail.setText(emailText.getText().toString());
            editPhone.setText(phoneText.getText().toString());
            stateSpinner.setSelection(stateSpinner.getSelectedItemPosition());
            districtSpinner.setSelection(districtSpinner.getSelectedItemPosition());
            street.setText(streetText.getText().toString());
            profileTypeSpinner.setSelection(profileTypeSpinner.getSelectedItemPosition());
            // Change button text to "Save"
            editButton.setText("Save");

            // Enable editing in fields
            editName.setEnabled(true);
            profileTypeSpinner.setEnabled(true);
            editEmail.setEnabled(true);
            editPhone.setEnabled(true);
            stateSpinner.setEnabled(true);
            districtSpinner.setEnabled(true);
            street.setEnabled(true);
        } else {
            // Save the changes when clicking "Save"
            updateProfileData();
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();  // Clears all saved data (like tokens, user info, etc.)
            editor.apply();
            logoutUser();

        }

    }
    private void showCustomDialog(String str) {
        Dialog dialog = new Dialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        dialog.setContentView(dialogView);

        TextView tv = dialogView.findViewById(R.id.dialog_text);
        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        if (dialogImage != null) {
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.nopostfound)
                    .into(dialogImage);
        }
        tv.setText(str);
        Button closeButton = dialogView.findViewById(R.id.dialog_button);

        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void updateProfileData() {
        fetchPostsAndUpdate();
        String url = "http://10.0.2.2:1010/api/auth/profile/" + userName + "/update";
        JSONObject profileData = new JSONObject();
        try {
            profileData.put("name", editName.getText().toString());
            profileData.put("email", editEmail.getText().toString());
            profileData.put("profileType", profileTypeSpinner.getSelectedItem().toString());
            profileData.put("phoneNumber", editPhone.getText().toString());
            profileData.put("state", stateSpinner.getSelectedItem().toString());
            profileData.put("district", districtSpinner.getSelectedItem().toString());
            profileData.put("streetOrColony", street.getText().toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, profileData,
                    response -> {
                        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                        fetchProfileData(userName);  // Refresh profile after saving
                    },
                    error -> {
                        showCustomDialog("SERVER PROBLEM or NETWORK ISSUE");
                        Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                    });

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
        startActivity(intent);
        finish(); // Close the SettingsActivity
    }
    private void fetchPostsAndUpdate() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:1010/api/posts/getall-posts-for-sale";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("",response.toString());
                        for (int i = 0; i < response.length(); i++) {


                            try {
                                Long id = response.getJSONObject(i).getLong("id");
                                String brokerName = response.getJSONObject(i).getString("brokerName");
                                String phoneNumber = response.getJSONObject(i).getString("phoneNumber");
                                String title = response.getJSONObject(i).getString("title");
                                String image = response.getJSONObject(i).getString("image");
                                String streetOrColony = response.getJSONObject(i).getString("streetOrColony");
                                String state = response.getJSONObject(i).getString("state");
                                String district = response.getJSONObject(i).getString("district");
                                String geolocation = response.getJSONObject(i).getString("geolocation");
                                String description = response.getJSONObject(i).getString("description");
                                double pricePerSqrFeet = response.getJSONObject(i).getDouble("pricePerSqrFeet");
                                double totalSqrFeet = response.getJSONObject(i).getDouble("totalSqrFeet");
                                double totalPrice = response.getJSONObject(i).getDouble("totalPrice");
                                boolean isForSale = response.getJSONObject(i).getBoolean("isForSale");
                                boolean isSold = response.getJSONObject(i).getBoolean("isSold");
                                String keyWords = response.getJSONObject(i).getString("keyWords");
                                String propertyType = response.getJSONObject(i).getString("propertyType");
                                // Modify this as necessary

                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                if(brokerName.equalsIgnoreCase(oldName)){
                                    post.setBrokerName(editName.getText().toString());
                                }
                                updatePostToServer(post.getId(),post);
                            } catch (JSONException e) {
                                Log.e("JSONError", e.toString());
                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastMaker.toast("In ProfileActivity unable to load background process posts",ProfileActivity.this);

                        if (error.networkResponse != null) {

                            // Server responded with an error
                            Log.e("VolleyError", "Error Code: " + error.networkResponse.statusCode);

                            Toast.makeText(ProfileActivity.this ,"Server error: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                        } else if (error.getCause() instanceof java.net.SocketTimeoutException) {
                            // Timeout exception (likely slow internet)
                            Log.e("VolleyError", "Timeout: " + error.getMessage());
                            Toast.makeText(ProfileActivity.this, "Request timed out. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other errors (e.g., network connection)
                            Log.e("VolleyError", "Error: " + error.getMessage());

                            Toast.makeText(ProfileActivity.this, "server or network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        requestQueue.add(jsonArrayRequest);
    }
    private void updatePostToServer(Long postId, Post post) {
        // Convert Post to JSON
        JSONObject postJson = new JSONObject();
        try {
            postJson.put("brokerName", post.getBrokerName());
            postJson.put("phoneNumber", post.getPhoneNumber());
            postJson.put("title", post.getTitle());
            postJson.put("image", post.getImage());
            postJson.put("streetOrColony", post.getStreetOrColony());
            postJson.put("state", post.getState());
            postJson.put("district", post.getDistrict());
            postJson.put("geolocation", post.getGeolocation());
            postJson.put("description", post.getDescription());
            postJson.put("pricePerSqrFeet", post.getPricePerSqrFeet());
            postJson.put("totalSqrFeet", post.getTotalSqrFeet());
            postJson.put("totalPrice", post.getTotalPrice());
            postJson.put("isForSale", post.getForSale());
            postJson.put("isSold", post.getSold());
            postJson.put("keyWords", post.getKeyWords());
            postJson.put("propertyType", post.getPropertyType());

            // Update the URL to include the post ID and change the request method to PUT
            String url = "http://10.0.2.2:1010/api/posts/update-post/" + postId;


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, postJson,
                    response -> {
                        Toast.makeText(ProfileActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    },
                    error -> {
                        Toast.makeText(ProfileActivity.this, "Failed to update post: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
