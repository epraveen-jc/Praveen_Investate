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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.praveen_investate.R;
import com.example.praveen_investate.database.DatabaseHelper;
import com.example.praveen_investate.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private final List<Post> postList;
    private final Context context;
    private DatabaseHelper databaseHelper;
    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post post = postList.get(position);
        Log.e("",post.getId()+"");
        holder.titleTextView.setText(post.getTitle());

        holder.agentName.setText(post.getAgentName());
        holder.phoneNumber.setText(post.getPhoneNumber()+"");
        if(post.getSold()){
            Glide.with(context)
                    .asGif()  // Specify that the resource is a GIF
                    .load(R.drawable.soldanime)  // URL or local file
                    .into(holder.dealButton);
        }else{
            // Set up the deal button listener
            holder.dealButton.setOnClickListener(v -> {
                sendNotification(post);
            });
        }


        StringBuilder sb = new StringBuilder();
        sb.append("Post Details:\n");
        sb.append("Agent Name: ").append(post.getAgentName()).append("\n");
        sb.append("Phone Number: ").append(post.getPhoneNumber()).append("\n");
        sb.append("Title: ").append(post.getTitle()).append("\n");
        sb.append("Location: ").append(post.getState()).append(", ").append(post.getDistrict()).append(", ").append(post.getStreetOrColony()).append("\n");
        sb.append("Geolocation: ").append(post.getGeolocation()).append("\n");
        sb.append("Description: ").append(post.getDescription()).append("\n");
        sb.append("Price Per Square Foot: $").append(post.getPricePerSqrFeet()).append("\n");
        sb.append("Total Square Feet: ").append(post.getTotalPrice()).append(" sq ft\n");
        sb.append("Total Price: $").append(post.getTotalPrice()).append("\n");
        sb.append("Property Type: ").append(post.getPropertyType()).append("\n");
        String str[] = (post.getCreatedAt()+"").split(" ");
        String date = str[0];

        sb.append("Post Uploaded On: ").append(date);

         holder.detailsText.setText(sb.toString());

         holder.detailsBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (holder.detailsText.getVisibility() == View.GONE) {
                     holder.detailsText.setVisibility(View.VISIBLE); // Show details
                 } else {
                     holder.detailsText.setVisibility(View.GONE); // Hide details
                 }
             }
         });
        holder.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(post.getGeolocation());
            }
        });
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber(post.getPhoneNumber());
            }
        });
        Glide.with(context)
                .load(post.getImage())
                .error(R.drawable.dummyhouse)
                .into(holder.imageView);// Load image using Glide
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
         ImageButton callBtn;
        TextView titleTextView;
        ImageView imageView;
        ImageButton dealButton , detailsBtn , locationBtn;
        TextView contentTextView , agentName , phoneNumber , detailsText ;


        MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.imageView);
            agentName = itemView.findViewById(R.id.agentName);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            detailsText = itemView.findViewById(R.id.detailsText);

            detailsBtn = itemView.findViewById(R.id.detailsBtn);
            locationBtn = itemView.findViewById(R.id.locationBtn);
            callBtn = itemView.findViewById(R.id.callBtn);
            dealButton = itemView.findViewById(R.id.dealButton);
        }
    }
}
