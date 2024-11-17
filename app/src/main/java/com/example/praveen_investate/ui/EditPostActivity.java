package com.example.praveen_investate.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.praveen_investate.R;
import com.example.praveen_investate.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EditPostActivity extends AppCompatActivity {

    private EditText brokerNameEditText;
    private EditText phoneNumberEditText;
    private EditText titleEditText;
    private EditText streetOrColonyEditText;
    private EditText stateEditText;
    private EditText districtEditText;
    private EditText geolocationEditText;
    private EditText descriptionEditText;
    private EditText pricePerSqrFeetEditText;
    private EditText totalSqrFeetEditText;
    private EditText totalPriceEditText;
    private Switch isForSaleSwitch;
    private ImageView imageView;

    private Uri imageUri;
    private Post post;
    private long id;

    // Create an ActivityResultLauncher for the image picker

    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imageView.setImageURI(imageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        // Initialize views
        brokerNameEditText = findViewById(R.id.brokerNameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        titleEditText = findViewById(R.id.titleEditText);
        streetOrColonyEditText = findViewById(R.id.streetOrColonyEditText);
        stateEditText = findViewById(R.id.stateEditText);
        districtEditText = findViewById(R.id.districtEditText);
        geolocationEditText = findViewById(R.id.geolocationEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        pricePerSqrFeetEditText = findViewById(R.id.pricePerSqrFeetEditText);
        totalSqrFeetEditText = findViewById(R.id.totalSqrFeetEditText);
        totalPriceEditText = findViewById(R.id.totalPriceEditText);
        isForSaleSwitch = findViewById(R.id.isForSaleSwitch);
        imageView = findViewById(R.id.imageView);

        Button uploadImageButton = findViewById(R.id.uploadImageButton);
        Button savePostButton = findViewById(R.id.savePostButton);

        // Get post data from intent
        post = (Post) getIntent().getSerializableExtra("post");
        if (post != null) {
            id = post.getId();
            populateFields(post);
        }

        // Set upload image button click listener
        uploadImageButton.setOnClickListener(v -> openFileChooser());

        // Set save post button click listener
        savePostButton.setOnClickListener(v -> savePost());
    }

    private void populateFields(Post post) {
        brokerNameEditText.setText(post.getBrokerName());
        phoneNumberEditText.setText(post.getPhoneNumber());
        titleEditText.setText(post.getTitle());
        streetOrColonyEditText.setText(post.getStreetOrColony());
        stateEditText.setText(post.getState());
        districtEditText.setText(post.getDistrict());
        geolocationEditText.setText(post.getGeolocation());
        descriptionEditText.setText(post.getDescription());
        pricePerSqrFeetEditText.setText(String.valueOf(post.getPricePerSqrFeet()));
        totalSqrFeetEditText.setText(String.valueOf(post.getTotalSqrFeet()));
        totalPriceEditText.setText(String.valueOf(post.getTotalPrice()));
        isForSaleSwitch.setChecked(post.getForSale());
        // Load the image using Glide or any other image loading library
         Glide.with(this).load(post.getImage()).into(imageView);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        selectImageLauncher.launch(Intent.createChooser(intent, "Select Image"));
        imageView.setVisibility(View.VISIBLE);
    }


    private void savePost() {
        String brokerName = brokerNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String title = titleEditText.getText().toString();
        String streetOrColony = streetOrColonyEditText.getText().toString();
        String state = stateEditText.getText().toString();
        String district = districtEditText.getText().toString();
        String geolocation = geolocationEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        Double pricePerSqrFeet = Double.parseDouble(pricePerSqrFeetEditText.getText().toString());
        Double totalSqrFeet = Double.parseDouble(totalSqrFeetEditText.getText().toString());
        Double totalPrice = Double.parseDouble(totalPriceEditText.getText().toString());
        boolean forSale = isForSaleSwitch.isChecked();

        // Create a new Post object or update the existing one
        if (post != null) {
            post.setBrokerName(brokerName);
            post.setPhoneNumber(phoneNumber);
            post.setTitle(title);
            post.setStreetOrColony(streetOrColony);
            post.setState(state);
            post.setDistrict(district);
            post.setGeolocation(geolocation);
            post.setDescription(description);
            post.setPricePerSqrFeet(pricePerSqrFeet);
            post.setTotalSqrFeet(totalSqrFeet);
            post.setTotalPrice(totalPrice);
            post.setIsForSale(forSale);
            // Update the image if a new one is selected
            if (imageUri != null) {
                post.setImage(imageUri.toString());
            }

            // TODO: Save the post to the database or send it back to the previous activity
            Toast.makeText(this, "Post updated successfully", Toast.LENGTH_SHORT).show();
        }
        sendPostToServer(id,post);
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
    private void sendPostToServer(Long postId, Post post) {
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
                        Toast.makeText(EditPostActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    },
                    error -> {
                        Toast.makeText(EditPostActivity.this, "Failed to update post: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
