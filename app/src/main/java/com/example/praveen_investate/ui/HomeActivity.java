package com.example.praveen_investate.ui;

import android.app.Dialog;

import android.content.Context;

import android.content.Intent;

import android.net.ConnectivityManager;

import android.net.NetworkInfo;

import android.os.Bundle;

import android.os.Handler;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.MotionEvent;

import android.view.View;

import android.view.inputmethod.InputMethodManager;

import android.widget.Button;

import android.widget.ImageButton;

import android.widget.ImageView;

import android.widget.ProgressBar;

import android.widget.SearchView;

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

import com.example.praveen_investate.adapter.NotificationAdapter;

import com.example.praveen_investate.model.PropertyType;
import com.example.praveen_investate.ui.AnalyticsActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList;
    private ProgressBar progressBar;
    private  TextView tvp;
    private ImageButton setting ,refresh ,notify, btnSearchByCat ,  searchBtnHomeBottomNavi , editprofile ,ana;
    private boolean isGifPlaying = false;
    private SearchView searchView;
    private DatabaseHelper databaseHelper;


    private final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        TextView tvProfileName = findViewById(R.id.profileName);
        TextView tvProfileType = findViewById(R.id.profileType);
        setting = findViewById(R.id.setting);
         progressBar = findViewById(R.id.progressBar);
        notify= findViewById(R.id.gifBell);
        refresh= findViewById(R.id.refreshdata);
        // Create new LayoutParams with the converted dimension
        ImageButton btnOpenUpload1 = findViewById(R.id.btnOpenUpload);
        searchView = findViewById(R.id.home_searchbar);
        btnSearchByCat = findViewById(R.id.searchByCat);
        ana = findViewById(R.id.ana);
        editprofile = findViewById(R.id.editprofile);
 tvp = findViewById(R.id.properties_available);


        try{
            if(databaseHelper.getProfile().getProfileType().equalsIgnoreCase("client")){
                notify.setVisibility(View.INVISIBLE);
            }else{
                notify.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            ToastMaker.toast("unable to hide notification ",this);
        }

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

        ana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(HomeActivity.this, AnalyticsActivity.class);
                    startActivity(intent);
                }, 2000);

            }
        });




        searchBtnHomeBottomNavi = findViewById(R.id.searchBtnHomeBottomNavi);

        searchBtnHomeBottomNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvp.setVisibility(View.GONE);
                activateSearchBar();
            }
        });




        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tvp.setVisibility(View.VISIBLE);
                hideSearchBar();

                new Handler().postDelayed(() -> {
                    tvp.setVisibility(View.GONE);
                }, 10000);
                return true; // Return true to indicate we handled the close
            }
        });



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

        Glide.with(this)
                .asBitmap() // Load as static image to show the first frame only
                .load(R.drawable.search_by_cat)  // Replace with your actual GIF file in drawable
                .apply(new RequestOptions().frame(0))  // Show only the first frame
                .into(btnSearchByCat);

        Glide.with(this)
                .asBitmap() // Load as static image to show the first frame only
                .load(R.drawable.search_anime)  // Replace with your actual GIF file in drawable
                .apply(new RequestOptions().frame(0))  // Show only the first frame
                .into(searchBtnHomeBottomNavi);

        Glide.with(this)
                .asBitmap() // Load as static image to show the first frame only
                .load(R.drawable.profile_for_bottom_nav)  // Replace with your actual GIF file in drawable
                .apply(new RequestOptions().frame(0))  // Show only the first frame
                .into(editprofile);

        Glide.with(this)
                .asBitmap() // Load as static image to show the first frame only
                .load(R.drawable.anaprogress)  // Replace with your actual GIF file in drawable
                .apply(new RequestOptions().frame(0))  // Show only the first frame
                .into(ana);


        setGifOnTouch(ana,R.drawable.anaprogress);

        setGifOnTouch(editprofile,R.drawable.profile_for_bottom_nav);
        setGifOnTouch(searchBtnHomeBottomNavi,R.drawable.search_anime);
        setGifOnTouch(refresh,R.drawable.refreshanime);
        setGifOnTouch(setting, R.drawable.settings);
        setGifOnTouch(notify,R.drawable.noti);
        setGifOnTouch(btnOpenUpload1,R.drawable.upload);
        setGifOnTouch(btnSearchByCat,R.drawable.search_by_cat);
        setupSearchView();




        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });




        btnSearchByCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastMaker.toast("Search Your Properties By Category..!",HomeActivity.this);
                new Handler().postDelayed(() -> {
                Intent intent = new Intent(HomeActivity.this,CategoryActivity.class);
                startActivity(intent);
            }, 2000);
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
                Intent intent = new Intent(HomeActivity.this,NotificationActivity.class);
                startActivity(intent);
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
                ToastMaker.toast("Upload Your Properties..!",HomeActivity.this);
                new Handler().postDelayed(() -> {
                    // Create an Intent to open UploadActivity
                    Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
                    startActivity(intent); // Start UploadActivity

                }, 1700);
                  }
        });





        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            ToastMaker.toast("Fetching Properties From Server ..!",HomeActivity.this);
            feed();
        }, 2000);




    }


    private void hideKeyboard() {
        getOnBackPressedDispatcher().onBackPressed();
    }



    private void hideSearchBar() {
        // Hide the SearchView and dismiss the keyboard
        searchView.setVisibility(View.GONE);


    }


    private void setupSearchView() {
        tvp.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setVisibility(View.GONE);
                feed(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                feed(newText);
                return true;
            }

        });
    }




    private void activateSearchBar() {
        tvp.setVisibility(View.GONE);
        // Show and focus on the SearchView
        if (searchView.getVisibility() == View.GONE) {
            searchView.setVisibility(View.VISIBLE);
        }

        searchView.requestFocus();
        searchView.setIconified(false); // Expands the search field

        // Show the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchView.findFocus(), InputMethodManager.SHOW_IMPLICIT);
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
                                if(databaseHelper.getProfile().getName().equals(response.getJSONObject(i).getString("brokerName"))){
                                    continue;
                                }
                                if(n > 0){
                                    for (int j = 0; j < n; j++) {

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
                                            if(brokerName.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }
                                            else if(propertyType.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }
                                            else if(description.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if(keyWords.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if(phoneNumber.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if(state.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if(streetOrColony.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if(district.toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if((pricePerSqrFeet+"").toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else
                                            if((totalSqrFeet+"").toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
                                                        description, pricePerSqrFeet, totalSqrFeet, totalPrice, isForSale, isSold, keyWords, propertyType);
                                                postList.add(post);
                                            }else if((totalPrice+"").toLowerCase().contains(arr[j].toLowerCase())){
                                                Post post = new Post(id ,brokerName, phoneNumber, title, image, streetOrColony, state, district, geolocation,
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

                        if(postList.size() != 0){

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
                        }, 5000);
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

                                try{
                                    if(databaseHelper.getProfile().getName().equals(response.getJSONObject(i).getString("brokerName"))){
                                        continue;
                                    }
                                } catch (Exception e) {

                                }

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
                                    postList.add(post);
                                } catch (JSONException e) {
                                    Log.e("JSONError", e.toString());
                                }

                        }
                        if(postList.size() == 0){
                            showCustomDialog("NO POST FOUND");
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
        tvp.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            tvp.setVisibility(View.GONE);
        }, 10000);
    }




}


