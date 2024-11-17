package com.example.praveen_investate.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.praveen_investate.R;
import com.example.praveen_investate.adapter.PostAdapter;
import com.example.praveen_investate.adapter.PostAdapterForPropertyWise;
import com.example.praveen_investate.database.DatabaseHelper;
import com.example.praveen_investate.model.Post;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PropertyListActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private PostAdapterForPropertyWise adapter;
    private List<Post> postList;
    private ProgressBar progressBar2;
    String propertyType = "";
    String selectedState ="";
    String selectedDistrict="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_list);
        databaseHelper = new DatabaseHelper(this);
        progressBar2 = findViewById(R.id.progressBar);
         propertyType = getIntent().getStringExtra("property_type");
        setTitle(propertyType + " Listings");
        progressBar2.setVisibility(View.VISIBLE);
        TextView filter = findViewById(R.id.filter);
        new Handler().postDelayed(() -> {
            feed();
        }, 1000);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStateDistrictDialog();
                if(selectedDistrict.isEmpty() || selectedState.isEmpty()){
                    progressBar2.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        feed();
                    }, 1000);
                }else{
                    progressBar2.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        feed(selectedState,selectedDistrict);
                    }, 1000);
                }


            }
        });


        // Use the propertyType to filter and load properties in this category from backend or database
    }
    private void setupSpinner(Spinner spinner, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResourceId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    private void showStateDistrictDialog() {
        // Inflate the custom dialog layout
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_state_district, null);
        builder.setView(dialogView);

        // Find the Spinners in the dialog layout
        Spinner spinnerState = dialogView.findViewById(R.id.spinner_state);
        Spinner spinnerDistrict = dialogView.findViewById(R.id.spinner_district);

        // Set up the adapters for the Spinners
        setupSpinner(spinnerState, R.array.states);
        setupSpinner(spinnerDistrict, R.array.districts);

        // Set positive button to dismiss dialog
        builder.setPositiveButton("OK", (dialog, which) -> {
            // You can get the selected state and district here if needed
            selectedState = spinnerState.getSelectedItem().toString();
            selectedDistrict = spinnerDistrict.getSelectedItem().toString();
            // Store or use the selected state and district as needed
        });

        // Show the dialog
        builder.setCancelable(false);  // Make it non-cancelable to ensure user selection
        builder.show();
    }
    private void feed(){
        recyclerView = findViewById(R.id.recyclerviewforpropertywise2);
        postList = new ArrayList<>();
        adapter = new PostAdapterForPropertyWise(this, postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressBar2.setVisibility(View.VISIBLE);
        fetchPosts();
    }

    private void feed(String state ,String dist){
        recyclerView = findViewById(R.id.recyclerviewforpropertywise2);
        postList = new ArrayList<>();
        adapter = new PostAdapterForPropertyWise(this, postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressBar2.setVisibility(View.VISIBLE);
        fetchPosts(state , dist);
    }
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
    private void fetchPosts() {
        if (!isConnectedToInternet()) {
            progressBar2.setVisibility(View.GONE);
            Toast.makeText(this, "No internet connection. Please check your settings.", Toast.LENGTH_LONG).show();
            return; // Exit early if no internet connection
        }
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
                                if(databaseHelper.getProfile().getName().equals(response.getJSONObject(i).getString("brokerName"))){
                                    continue;
                                }
                                if(response.getJSONObject(i).getString("propertyType").trim().equalsIgnoreCase(propertyType.trim())) {
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

                                        Post post = new Post(id, brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                        postList.add(post);
                                    } catch (JSONException e) {
                                        Log.e("JSONError", e.toString());
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if(postList.size() == 0){
                            showCustomDialog("NO POST FOUND");
                        }
                        adapter.notifyDataSetChanged();
                        progressBar2.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar2.setVisibility(View.GONE);
                        if (error.networkResponse != null) {

                            // Server responded with an error
                            Log.e("VolleyError", "Error Code: " + error.networkResponse.statusCode);
                            showCustomDialog(""+ error.networkResponse.statusCode);

                            Toast.makeText(PropertyListActivity.this, "Server error: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                        } else if (error.getCause() instanceof java.net.SocketTimeoutException) {
                            // Timeout exception (likely slow internet)
                            Log.e("VolleyError", "Timeout: " + error.getMessage());
                            Toast.makeText(PropertyListActivity.this, "Request timed out. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other errors (e.g., network connection)
                            Log.e("VolleyError", "Error: " + error.getMessage());
                            showCustomDialog("No Post FOUND!");
                            Toast.makeText(PropertyListActivity.this, "server or network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        requestQueue.add(jsonArrayRequest);
    }
    private void fetchPosts(String state , String dist) {
        if (!isConnectedToInternet()) {
            progressBar2.setVisibility(View.GONE);
            Toast.makeText(this, "No internet connection. Please check your settings.", Toast.LENGTH_LONG).show();
            return; // Exit early if no internet connection
        }
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
                                if(databaseHelper.getProfile().getName().equals(response.getJSONObject(i).getString("brokerName"))){
                                    continue;
                                }
                                if(response.getJSONObject(i).getString("propertyType").trim().equalsIgnoreCase(propertyType.trim())
                                  && response.getJSONObject(i).getString("state").equalsIgnoreCase(state)
                                        && response.getJSONObject(i).getString("district").equalsIgnoreCase(dist)
                                ) {
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

                                        Post post = new Post(id, brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                        postList.add(post);
                                    } catch (JSONException e) {
                                        Log.e("JSONError", e.toString());
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if(postList.size() == 0){
                            showCustomDialog("NO POST FOUND");
                        }
                        adapter.notifyDataSetChanged();
                        progressBar2.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar2.setVisibility(View.GONE);
                        if (error.networkResponse != null) {

                            // Server responded with an error
                            Log.e("VolleyError", "Error Code: " + error.networkResponse.statusCode);
                            showCustomDialog(""+ error.networkResponse.statusCode);

                            Toast.makeText(PropertyListActivity.this, "Server error: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                        } else if (error.getCause() instanceof java.net.SocketTimeoutException) {
                            // Timeout exception (likely slow internet)
                            Log.e("VolleyError", "Timeout: " + error.getMessage());
                            Toast.makeText(PropertyListActivity.this, "Request timed out. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other errors (e.g., network connection)
                            Log.e("VolleyError", "Error: " + error.getMessage());
                            showCustomDialog("No Post FOUND!");
                            Toast.makeText(PropertyListActivity.this, "server or network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        requestQueue.add(jsonArrayRequest);
    }
}
