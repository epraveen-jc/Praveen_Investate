package com.example.praveen_investate.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.praveen_investate.R;
import com.example.praveen_investate.database.DatabaseHelper;
import com.example.praveen_investate.model.Post;
import com.example.praveen_investate.model.Profile;
import com.example.praveen_investate.utils.ProfileManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    private Uri imageUri; // URI of the selected image
    private String uploadedImageUrl; // URL after the image is uploaded

    private EditText etTitle, etStreetOrColony, etKeyWords ,etDescription, etPricePerSqrFeet, etTotalSqrFeet, etTotalPrice;
    private Spinner spinnerState, spinnerDistrict ,spinnerPropertyType;
    private Button btnUpload, btnSelectImage, btnSelectLocation;
    private ImageView ivSelectedImage;
    private FusedLocationProviderClient fusedLocationClient; // Client for getting location
    private Location currentLocation; // Store current location
    TextView tvName , tvNumber;
    // ActivityResultLauncher for selecting an image
    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    ivSelectedImage.setImageURI(imageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Initialize views
        tvName = findViewById(R.id.brokerName);

        etTitle = findViewById(R.id.etTitle);
        etKeyWords = findViewById(R.id.etKeyWords);
        etStreetOrColony = findViewById(R.id.etStreetOrColony);
        etDescription = findViewById(R.id.etDescription);
        etPricePerSqrFeet = findViewById(R.id.etPricePerSqrFeet);
        etTotalSqrFeet = findViewById(R.id.etTotalSqrFeet);
        etTotalPrice = findViewById(R.id.etTotalPrice);
        spinnerState = findViewById(R.id.spinnerState);
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        btnUpload = findViewById(R.id.btnUpload);
        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        btnSelectImage = findViewById(R.id.btnSelectImage); // Make sure you have this button in your layout
        ivSelectedImage = findViewById(R.id.imagePreview);
        spinnerPropertyType = findViewById(R.id.spinnerPropertyType);

        // TextWatcher for monitoring changes in etPricePerSqrFeet and etTotalSqrFeet
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Calculate total price whenever text in etPricePerSqrFeet or etTotalSqrFeet changes
                calculateTotalPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        };

        // Attach TextWatcher to etPricePerSqrFeet and etTotalSqrFeet
        etPricePerSqrFeet.addTextChangedListener(watcher);
        etTotalSqrFeet.addTextChangedListener(watcher);


        try{

            tvName.setText(new DatabaseHelper(this).getProfile().getName());
            tvNumber.setText(new DatabaseHelper(this).getProfile().getPhoneNumber());
        }catch(Exception e){
            ToastMaker.toast(e.toString(),this);
            Log.e("",e.toString());

        }


        // Set up the spinner adapters
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(stateAdapter);

        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(this,
                R.array.districts, android.R.layout.simple_spinner_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);
        // Load property types from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.property_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPropertyType.setAdapter(adapter);
        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Button to select image
        btnSelectImage.setOnClickListener(v -> openFileChooser());

        // Button to select location
        btnSelectLocation.setOnClickListener(v -> getCurrentLocation());

        // Button to upload post
        btnUpload.setOnClickListener(v -> uploadPost());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        selectImageLauncher.launch(Intent.createChooser(intent, "Select Image"));
        ivSelectedImage.setVisibility(View.VISIBLE);
    }

    private void calculateTotalPrice() {
        String pricePerSqrFeetText = etPricePerSqrFeet.getText().toString();
        String totalSqrFeetText = etTotalSqrFeet.getText().toString();

        // Check if both inputs are non-empty
        if (!pricePerSqrFeetText.isEmpty() && !totalSqrFeetText.isEmpty()) {
            try {
                // Parse input values to double and calculate the total price
                double pricePerSqrFeet = Double.parseDouble(pricePerSqrFeetText);
                double totalSqrFeet = Double.parseDouble(totalSqrFeetText);
                double totalPrice = pricePerSqrFeet * totalSqrFeet;

                // Display total price in etTotalPrice
                etTotalPrice.setText(String.valueOf(totalPrice));
            } catch (NumberFormatException e) {
                // If input is not a valid number, clear the result
                etTotalPrice.setText("");
            }
        } else {
            // Clear etTotalPrice if any of the inputs is empty
            etTotalPrice.setText("");
        }
    }
    private void getCurrentLocation() {
        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            fetchLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location; // Store the current location
                        Toast.makeText(this, "Location Retrieved: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    } else {
                        // Request location updates if last location is null
                        Toast.makeText(this, "Unable to retrieve last location, requesting location update...", Toast.LENGTH_SHORT).show();
                        requestLocationUpdates();
                    }
                });
    }
    private void requestLocationUpdates() {
        // Use the new constructor without `.create()`
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation = location;
                        Toast.makeText(getApplicationContext(), "Location Updated: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        fusedLocationClient.removeLocationUpdates(this); // Stop updates after getting a location
                        break;
                    }
                }
            }
        }, Looper.getMainLooper());
    }

    private void uploadPost() {
        // Validate inputs
        if (validateInputs()) {
            // Gather data from input fields

            String brokerName = new DatabaseHelper(this).getProfile().getName(); // Replace with actual broker name or add input field
            String phoneNumber ="+91"+(new DatabaseHelper(this).getProfile().getPhoneNumber()); // Replace with actual phone number or add input field
            String title = etTitle.getText().toString();
            String propertyType = spinnerPropertyType.getSelectedItem().toString();
            String streetOrColony = etStreetOrColony.getText().toString();
            String state = spinnerState.getSelectedItem().toString();
            String district = spinnerDistrict.getSelectedItem().toString();
            String geolocation = currentLocation.getLatitude() + "," + currentLocation.getLongitude(); // Get the geolocation from current location
            String description = etDescription.getText().toString();
            double pricePerSqrFeet = Double.parseDouble(etPricePerSqrFeet.getText().toString());
            double totalSqrFeet = Double.parseDouble(etTotalSqrFeet.getText().toString());
            double totalPrice = Double.parseDouble(etTotalPrice.getText().toString());
            boolean isForSale = true; // Assuming this is always true when creating a post
            boolean isSold = false; // New post is not sold
            String keywords = etKeyWords.getText().toString();
            /***
             *
             * space for logic upload image into s3
             * using imageUri
             *
             */


            // Create the Post object after image is uploaded
            Post post = new Post();
            post.setBrokerName(brokerName);
            post.setPhoneNumber(phoneNumber);
            post.setTitle(title);
            post.setImage("prafileimage");
            post.setStreetOrColony(streetOrColony);
            post.setState(state);
            post.setDistrict(district);
            post.setGeolocation(geolocation);
            post.setDescription(description);
            post.setPricePerSqrFeet(pricePerSqrFeet);
            post.setTotalSqrFeet(totalSqrFeet);
            post.setTotalPrice(totalPrice);
            post.setIsForSale(isForSale);
            post.setIsSold(isSold);
            post.setPropertyType(propertyType);
            post.setKeyWords(keywords);

            // Convert Post to JSON and send to server
            sendPostToServer(post);
        }
    }



    private void sendPostToServer(Post post) {
        // Convert Post to JSON
        JSONObject postJson = new JSONObject();
        try {
            postJson.put("brokerName", post.getBrokerName());
            postJson.put("phoneNumber", post.getPhoneNumber());
            postJson.put("title", post.getTitle());
            postJson.put("image", post.getImage()); // Now this will be base64-encoded string
            postJson.put("streetOrColony", post.getStreetOrColony());
            postJson.put("state", post.getState());
            postJson.put("district", post.getDistrict());
            postJson.put("geolocation", post.getGeolocation());
            postJson.put("description", post.getDescription());
            postJson.put("pricePerSqrFeet", post.getPricePerSqrFeet());
            postJson.put("totalSqrFeet", post.getTotalSqrFeet());
            postJson.put("totalPrice", post.getTotalPrice());
            postJson.put("isForSale", true);
            postJson.put("isSold", false);
            postJson.put("keyWords", post.getKeyWords());
            postJson.put("propertyType", post.getPropertyType());

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:1010/api/posts/create-post", postJson,
                    response -> {
                        Toast.makeText(UploadActivity.this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    },
                    error -> {
                        Toast.makeText(UploadActivity.this, "Failed to upload post: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (currentLocation == null) {
            Toast.makeText(this, "Please retrieve the current location.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a title.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Add more validation as needed for other fields
        return true;
    }



}
