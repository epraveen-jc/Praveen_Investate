package com.example.praveen_investate.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.praveen_investate.R;
import com.example.praveen_investate.database.DatabaseHelper;
import com.example.praveen_investate.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PostAdapterForAgents extends RecyclerView.Adapter<PostAdapterForAgents.MyViewHolder> {

    private final List<Post> postList;
    private final Context context;
    private DatabaseHelper databaseHelper;
    public PostAdapterForAgents(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_for_agent, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post post = postList.get(position);
        Log.e("",post.getId()+"");
        holder.titleTextView.setText(post.getTitle());

        holder.agentName.setText(post.getAgentName());
        holder.phoneNumber.setText(post.getPhoneNumber()+"");
        holder.deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(post.getId());
            }
        });
        holder.setSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markPostAsSold(post.getId());
            }
        });

        Glide.with(context)
                .load(post.getImage())
                .error(R.drawable.dummyhouse)
                .into(holder.imageView);// Load image using Glide
    }
    // Method to delete a post by postId
    private void deletePost(Long postId) {
        String url = "http://10.0.2.2:1010/api/posts/delete-post/" + postId;

        // Creating the DELETE request
        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        Log.d("Volley Response", "Response: " + response);
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error responses
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                // Post not found
                                Toast.makeText(context, "Post not found.", Toast.LENGTH_SHORT).show();
                            } else if (statusCode == 400) {
                                // Other server errors (like failure in deletion)
                                String errorMessage = "Failed to delete post: ";
                                if (error.networkResponse.data != null) {
                                    try {
                                        errorMessage += new String(error.networkResponse.data, "UTF-8");
                                    } catch (Exception e) {
                                        Log.e("Volley Error", "Error parsing error response.");
                                    }
                                }
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                // Any other server error
                                Toast.makeText(context, "Server error: " + statusCode, Toast.LENGTH_SHORT).show();
                            }
                        } else if (error.getCause() instanceof java.net.SocketTimeoutException) {
                            // Timeout error
                            Toast.makeText(context, "Request timed out. Please try again.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Network-related errors
                            Toast.makeText(context, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Adding the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(deleteRequest);
    }

    // Method to mark a post as sold
    private void markPostAsSold(Long postId) {
        String url = "http://10.0.2.2:1010/api/posts/mark-sold/" + postId;

        // Creating the PUT request
        JsonObjectRequest putRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,  // No request body is needed here
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            // Assuming your Post object has fields like "id", "title", "isSold" etc.
                            Long id = response.getLong("id");
                            String title = response.getString("title");
                            boolean isSold = response.getBoolean("isSold");

                            // You can update your UI or data here as needed
                            Log.d("Volley Response", "Post marked as sold: " + title);
                            Toast.makeText(context, "Post marked as sold!", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "JSON parsing error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error responses
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                // Post not found
                                Toast.makeText(context, "Post not found.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Other errors
                                Log.e("Volley Error", "Error Code: " + statusCode);
                                Toast.makeText(context, "Server error: " + statusCode, Toast.LENGTH_SHORT).show();
                            }
                        } else if (error.getCause() instanceof java.net.SocketTimeoutException) {
                            // Timeout error
                            Toast.makeText(context, "Request timed out. Please try again.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other network errors
                            Toast.makeText(context, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Adding the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(putRequest);
    }

    private void callPhoneNumber(String phoneNumber) {
        String dial = "tel:" + phoneNumber;
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(dial));
        context.startActivity(callIntent);
    }
    private void openMap(String geolocation) {
        // Construct the URI for Google Maps
        String uri = "http://maps.google.com/maps?q=" + geolocation; // Use q parameter for a marker
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setPackage("com.google.android.apps.maps");

        // Check if there is an app that can handle this intent
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, "Google Maps is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
    private void sendNotification(Post post) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "http://10.0.2.2:1010/api/notifications/create-agent-notification";

        // Build the URL with query parameters
        String fullUrl = url + "?postId=" + post.getId() +
                "&clientName=" + Uri.encode(databaseHelper.getProfile().getName()) +
                "&agentName=" + Uri.encode(post.getAgentName()) +
                "&clientPhoneNumber=" + Uri.encode(post.getPhoneNumber());

        // Create the JSON object request without params
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, fullUrl, null,
                response -> {
                    // Handle the response from the server
                    Toast.makeText(context, "Notification sent successfully!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle error
                    Log.e("Notification Error", error.toString());
                    Toast.makeText(context, "Failed to send notification: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView imageView;
        Button setSold , updatePost , deletePost;
        TextView contentTextView , agentName , phoneNumber , detailsText ;


        MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.imageView);
            agentName = itemView.findViewById(R.id.agentName);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);

            setSold = itemView.findViewById(R.id.set_sold);
            deletePost = itemView.findViewById(R.id.delete_post);
            updatePost = itemView.findViewById(R.id.update_post);
        }
    }
}
