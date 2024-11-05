package com.example.praveen_investate.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import com.bumptech.glide.request.RequestOptions;
import com.example.praveen_investate.adapter.PostAdapter;
import com.example.praveen_investate.database.DatabaseHelper;
import com.example.praveen_investate.model.Notification;
import com.example.praveen_investate.model.Post;
import com.example.praveen_investate.model.Profile;
import com.example.praveen_investate.utils.ProfileManager;
import com.example.praveen_investate.adapter.NotificationAdapter;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList;
    private ProgressBar progressBar;
    private ImageButton setting ,refresh ,notify,searchBtn;
    private boolean isGifPlaying = false;
    private DatabaseHelper databaseHelper;

    private final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Profile profile;

        databaseHelper = new DatabaseHelper(this);
        setContentView(R.layout.activity_home);
        setting = findViewById(R.id.setting);
         progressBar = findViewById(R.id.progressBar);
        notify= findViewById(R.id.gifBell);
       refresh= findViewById(R.id.refreshdata);
        // Create new LayoutParams with the converted dimension
        ImageButton btnOpenUpload1 = findViewById(R.id.btnOpenUpload);

        TextView searchText = findViewById(R.id.search_edit_text);
        searchBtn = findViewById(R.id.searchBtn);
        TextView tvProfileName = findViewById(R.id.profileName);
        TextView tvProfileType = findViewById(R.id.profileType);
        Glide.with(this)
                .asBitmap() // Load as static image to show the first frame only
                .load(R.drawable.refreshanime ) // Replace with your actual GIF file in drawable
                .apply(new RequestOptions().frame(0))  // Show only the first frame
                .into(refresh);
        Glide.with(this)
                .asBitmap() // Load as static image to show the first frame only
                .load(R.drawable.noti) // Replace with your actual GIF file in drawable
                .apply(new RequestOptions().frame(0))  // Show only the first frame
                .into(notify);

        Glide.with(this)
                .asBitmap() // Load as static image to show the first frame only
                .load(R.drawable.settings)  // Replace with your actual GIF file in drawable
                .apply(new RequestOptions().frame(0))  // Show only the first frame
                .into(setting);
        Glide.with(this)
                .asBitmap() // Load as static image to show the first frame only
                .load(R.drawable.upload)  // Replace with your actual GIF file in drawable
                .apply(new RequestOptions().frame(0))  // Show only the first frame
                .into(btnOpenUpload1);


        setGifOnTouch(refresh,R.drawable.refreshanime);
        setGifOnTouch(setting, R.drawable.settings);
        setGifOnTouch(notify,R.drawable.noti);
        setGifOnTouch(btnOpenUpload1,R.drawable.upload);
        searchBtn.setImageResource(R.drawable.search);

        try{
            if(new DatabaseHelper(this).getProfile().getName().isEmpty()){
                tvProfileName.setText("Praveen");
            }else{
                tvProfileName.setText(new DatabaseHelper(this).getProfile().getName());

            }
        }catch(Exception e){
            ToastMaker.toast(e.toString(),this);
        }
        try{
            if(new DatabaseHelper(this).getProfile().getName().isEmpty()){
                tvProfileType.setText("Owner");
            }else{
                tvProfileType.setText(new DatabaseHelper(this).getProfile().getProfileType());
            }
        }catch(Exception e){
            ToastMaker.toast(e.toString(),this);
        }

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchText.getText().toString().isEmpty()){
                    ToastMaker.toast("Please enter a text in search field!..",v.getContext());
                    Log.e("searchbtn","searchTxt is empty");
                }else{
                    feedZeroPost();
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                            progressBar.setVisibility(View.GONE);
                            feed(searchText.getText().toString());
                        }, 1500);

                    }
                }


        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }, 1200);

            }
        });

        notify.setOnClickListener(v -> {
            //Notification
            new Handler().postDelayed(() -> {
                fetchNotifications();
            }, 1000);

        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feed();
            }
        });
        // Set an OnClickListener to handle the button click
        btnOpenUpload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(() -> {
                    // Create an Intent to open UploadActivity
                    Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
                    startActivity(intent); // Start UploadActivity

                }, 1700);
                  }
        });
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            feed();
        }, 1000);
    }
    private void fetchNotifications() {
        if (!isConnectedToInternet()) {
            Toast.makeText(this, "No internet connection. Please check your settings.", Toast.LENGTH_LONG).show();
            return; // Exit early if no internet connection
        }

        // Assuming you have a way to determine whether the user is an agent or a client
        String userType = new DatabaseHelper(this).getProfile().getProfileType(); // This should return either "Agent" or "Client"
        String userName = new DatabaseHelper(this).getProfile().getName(); // Get the current user's name

        String url="";
        if ("owner".equals(userType.toLowerCase()) || "agent".equals(userType.toLowerCase())) {
            url = "http://10.0.2.2:1010/api/notifications/agent/" + userName;
        }
        Log.e("", url);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("", response.toString());
                        List<Notification> notifications = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                if(databaseHelper.getProfile().getName().equals(response.getJSONObject(i).getString("clientName"))){
                                    continue;
                                }

                                String clientName = response.getJSONObject(i).getString("clientName");
                                String clientPhoneNumber = response.getJSONObject(i).getString("clientPhoneNumber");
                                String message = response.getJSONObject(i).getString("message");
                                notifications.add(new Notification(clientName, clientPhoneNumber, message));
                            } catch (JSONException e) {
                                Log.e("JSONError", e.toString());
                            }
                        }

                        // Display notifications in a dialog or another activity
                        showNotificationDialog(notifications);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error: " + error.getMessage());
                        Toast.makeText(HomeActivity.this, "Error fetching notifications", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void showNotificationDialog(List<Notification> notifications) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_notifications);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        NotificationAdapter adapter = new NotificationAdapter(this, notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        dialog.show();
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
    private void showNoPostFoundDialog() {
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
        tv.setText("NO POST FOUND");
        Button closeButton = dialogView.findViewById(R.id.dialog_button);

        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
    private void setGifOnTouch(ImageButton button, int gifResource) {
        button.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!isGifPlaying) {
                        Glide.with(this)
                                .asGif()
                                .load(gifResource)
                                .into(button);
                        isGifPlaying = true;
                        handler.postDelayed(() -> {
                            Glide.with(this)
                                    .asBitmap()
                                    .load(gifResource)
                                    .apply(new RequestOptions().frame(0))
                                    .into(button);
                            isGifPlaying = false;
                        }, 1800);
                    }
                    break;
            }

            return false;
        });

    }


    private void feed(){
        recyclerView = findViewById(R.id.recyclerview);
        postList = new ArrayList<>();
        adapter = new PostAdapter(this, postList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        fetchPosts();
    }
    private void feed(String str){
        recyclerView = findViewById(R.id.recyclerview);
        postList = new ArrayList<>();
        adapter = new PostAdapter(this, postList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        fetchPosts(str);
    }
    private void feedZeroPost(){
        recyclerView = findViewById(R.id.recyclerview);
        postList = new ArrayList<>();
        adapter = new PostAdapter(this, postList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);

    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchPosts() {
        if (!isConnectedToInternet()) {
            progressBar.setVisibility(View.GONE);
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
                                if(databaseHelper.getProfile().getName().equals(response.getJSONObject(i).getString("agentName"))){
                                    continue;
                                }
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

                                    Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                            description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                    postList.add(post);
                                } catch (JSONException e) {
                                    Log.e("JSONError", e.toString());
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
                            Toast.makeText(HomeActivity.this, "Server error: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                        } else if (error.getCause() instanceof java.net.SocketTimeoutException) {
                            // Timeout exception (likely slow internet)
                            Log.e("VolleyError", "Timeout: " + error.getMessage());
                            Toast.makeText(HomeActivity.this, "Request timed out. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other errors (e.g., network connection)
                            Log.e("VolleyError", "Error: " + error.getMessage());
                            showCustomDialog();
                            Toast.makeText(HomeActivity.this, "server or network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        requestQueue.add(jsonArrayRequest);
    }
    private void fetchPosts(String str) {
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
                        String arr[] = str.split(" ");
                        int n = arr.length;

                        Log.e("",response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                if(databaseHelper.getProfile().getName().equals(response.getJSONObject(i).getString("agentName"))){
                                    continue;
                                }
                                if(n > 0){
                                    for (int j = 0; j < n; j++) {

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
                                            if(agentName.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else if(description.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if(keyWords.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if(phoneNumber.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if(state.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if(streetOrColony.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if(district.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if((pricePerSqrFeet+"").toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if((totalSqrFeet+"").toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else if((totalPrice+"").toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,agentName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }

                                        } catch (JSONException e) {
                                            Log.e("JSONError", e.toString());

                                        }


                                    }
                                }




                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }


                        }

                        if(postList.size() == 0){
                            showNoPostFoundDialog();
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
                            Toast.makeText(HomeActivity.this, "Server error: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                        } else if (error.getCause() instanceof java.net.SocketTimeoutException) {
                            // Timeout exception (likely slow internet)
                            Log.e("VolleyError", "Timeout: " + error.getMessage());
                            Toast.makeText(HomeActivity.this, "Request timed out. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other errors (e.g., network connection)
                            Log.e("VolleyError", "Error: " + error.getMessage());
                            showCustomDialog();
                            Toast.makeText(HomeActivity.this, "server or network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        requestQueue.add(jsonArrayRequest);
    }

}

