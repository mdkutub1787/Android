package com.kutubuddin.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kutubuddin.myapplication.DetailActivity;
import com.kutubuddin.myapplication.R;
import com.kutubuddin.myapplication.model.DataClass;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList != null ? dataList : new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass data = dataList.get(position);

        // Load the image using Glide
        Glide.with(context)
                .load(data.getDataImage())
                .placeholder(R.drawable.error_image) // Placeholder image
                .error(R.drawable.error_image) // Error image
                .into(holder.recImage);

        // Set text fields
        holder.recTitle.setText(data.getDataTitle() != null ? data.getDataTitle() : "");
        holder.recDesc.setText(data.getDataDesc() != null ? data.getDataDesc() : "");
        holder.recLang.setText(data.getDataLang() != null ? data.getDataLang() : "");

        // Set click listener for the card
        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("Image", data.getDataImage());
            intent.putExtra("Description", data.getDataDesc());
            intent.putExtra("Title", data.getDataTitle());
            intent.putExtra("Key", data.getKey());
            intent.putExtra("Language", data.getDataLang());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Method to update data using DiffUtil for efficient list updates
    public void updateDataList(ArrayList<DataClass> newDataList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return dataList.size();
            }

            @Override
            public int getNewListSize() {
                return newDataList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return dataList.get(oldItemPosition).getKey()
                        .equals(newDataList.get(newItemPosition).getKey());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                DataClass oldData = dataList.get(oldItemPosition);
                DataClass newData = newDataList.get(newItemPosition);
                return oldData.equals(newData);
            }
        });

        this.dataList.clear(); // Clear the existing list
        this.dataList.addAll(newDataList); // Add the new filtered list
        diffResult.dispatchUpdatesTo(this); // Notify the adapter of changes
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage;
        TextView recTitle, recDesc, recLang;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recImage = itemView.findViewById(R.id.recImage);
            recCard = itemView.findViewById(R.id.recCard);
            recDesc = itemView.findViewById(R.id.recDesc);
            recLang = itemView.findViewById(R.id.recLang);
            recTitle = itemView.findViewById(R.id.recTitle);
        }
    }
}
