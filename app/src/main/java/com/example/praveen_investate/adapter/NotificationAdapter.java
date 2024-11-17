package com.example.praveen_investate.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.praveen_investate.R;
import com.example.praveen_investate.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.nameTextView.setText("From "+notification.getClientName());
        holder.messageTextView.setText("Read Message");
        holder.tvClientNumber.setText("Client Number: " + notification.getClientPhoneNumber());
        // Set the onClickListener to initiate a call
        holder.tvClientNumber.setOnClickListener(v -> {
            String clientNumber = notification.getClientPhoneNumber();
            initiateCall(clientNumber);
        });
        holder.messageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.messageTextView.getText().equals("Read Message")){
                    holder.messageTextView.setText(notification.getMessage());
                }else{
                    holder.messageTextView.setText("Read Message");
                }

            }
        });

    }

    private void initiateCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(callIntent);
    }
    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView , tvClientNumber;
        TextView messageTextView;
        Button acceptButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.notificationName);
            messageTextView = itemView.findViewById(R.id.notificationMessage);
            tvClientNumber = itemView.findViewById(R.id.tvClientNumber);

        }
    }
}