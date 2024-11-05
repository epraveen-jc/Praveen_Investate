package com.example.praveen_investate.ui;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.praveen_investate.R;
import com.example.praveen_investate.adapter.PostAdapter;
import com.example.praveen_investate.adapter.PostAdapterForAgents;
import com.example.praveen_investate.database.DatabaseHelper;
import com.example.praveen_investate.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapterForAgents adapter;
    private DatabaseHelper databaseHelper;
    private List<Post> postList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);

        postList = new ArrayList<>();
        adapter = new PostAdapterForAgents(this, postList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        feed();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void feed() {
        recyclerView = findViewById(R.id.recyclerview);
        postList = new ArrayList<>();
        adapter = new PostAdapterForAgents(this, postList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        fetchPosts();
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

    private void fetchPosts() {
        if (!isConnectedToInternet()) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "No internet connection. Please check your settings.", Toast.LENGTH_LONG).show();
            return; // Exit early if no internet connection
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:1010/api/posts/getall-posts";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("", response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                if (databaseHelper.getProfile().getName().equals(response.getJSONObject(i).getString("agentName"))) {
                                    try {
                                        Long id = response.getJSONObject(i).getLong("id");
                                        String agentName = response.getJSONObject(i).getString("agentName");
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

                                        Post post = new Post(id, agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
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

                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        if (error.networkResponse != null) {

                            // Server responded with an error
                            Log.e("VolleyError", "Error Code: " + error.networkResponse.statusCode);
                            showCustomDialog();
                            Toast.makeText(MyPostsActivity.this, "Server error: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                        } else if (error.getCause() instanceof java.net.SocketTimeoutException) {
                            // Timeout exception (likely slow internet)
                            Log.e("VolleyError", "Timeout: " + error.getMessage());
                            Toast.makeText(MyPostsActivity.this, "Request timed out. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other errors (e.g., network connection)
                            Log.e("VolleyError", "Error: " + error.getMessage());
                            showCustomDialog();
                            Toast.makeText(MyPostsActivity.this, "server or network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        requestQueue.add(jsonArrayRequest);
    }
}