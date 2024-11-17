package com.example.praveen_investate.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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
import com.example.praveen_investate.R;
import com.example.praveen_investate.adapter.NotificationAdapter;
import com.example.praveen_investate.database.DatabaseHelper;
import com.example.praveen_investate.model.Notification;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private  DatabaseHelper databaseHelper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        databaseHelper= new DatabaseHelper(this);
        fetchNotifications();

    }
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void fetchNotifications() {
        if (!isConnectedToInternet()) {
            Toast.makeText(this, "No internet connection. Please check your settings.", Toast.LENGTH_LONG).show();
            return; // Exit early if no internet connection
        }

        // Assuming you have a way to determine whether the user is an broker or a client
        String userType = new DatabaseHelper(this).getProfile().getProfileType(); // This should return either "Broker" or "Client"
        String userName = new DatabaseHelper(this).getProfile().getName(); // Get the current user's name

        String url="";
        if ("owner".equals(userType.toLowerCase()) || "broker".equals(userType.toLowerCase())) {
            url = "http://10.0.2.2:1010/api/notifications/broker/" + userName;
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
                                Log.e("homefetchnoti",response.getJSONObject(i).toString());
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
                        showNotificationActivity(notifications);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error: " + error.getMessage());
                        Toast.makeText(NotificationActivity.this, "Error fetching notifications", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void showNotificationActivity(List<Notification> notifications) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNoti);
        NotificationAdapter adapter = new NotificationAdapter(this, notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
