package com.example.praveen_investate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.praveen_investate.R;
import com.example.praveen_investate.model.PropertyType;
import com.example.praveen_investate.ui.PropertyListActivity;

import java.util.List;

public class PropertyTypeAdapter extends RecyclerView.Adapter<PropertyTypeAdapter.PropertyTypeViewHolder> {

    private List<PropertyType> propertyTypes;
    private Context context;

    public PropertyTypeAdapter(Context context, List<PropertyType> propertyTypes) {
        this.context = context;
        this.propertyTypes = propertyTypes;
    }

    @Override
    public PropertyTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property_type, parent, false);
        return new PropertyTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyTypeViewHolder holder, int position) {
        PropertyType propertyType = propertyTypes.get(position);
        holder.propertyName.setText(propertyType.getName());
        holder.propertyImage.setImageResource(propertyType.getImageResId());

        // Add onClick listener to open properties in that category
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PropertyListActivity.class);
            intent.putExtra("property_type", propertyType.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return propertyTypes.size();
    }

    public static class PropertyTypeViewHolder extends RecyclerView.ViewHolder {
        TextView propertyName;
        ImageView propertyImage;

        public PropertyTypeViewHolder(View itemView) {
            super(itemView);
            propertyName = itemView.findViewById(R.id.property_name);
            propertyImage = itemView.findViewById(R.id.property_image);
        }
    }
}

