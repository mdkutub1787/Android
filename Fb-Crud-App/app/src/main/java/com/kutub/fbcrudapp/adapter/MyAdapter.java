package com.kutub.fbcrudapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kutub.fbcrudapp.DetailActivity;
import com.kutub.fbcrudapp.R;
import com.kutub.fbcrudapp.model.DataClass;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;
    private List<DataClass> originalList; // To store original data

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList != null ? dataList : new ArrayList<>();
        this.originalList = new ArrayList<>(this.dataList); // Copy original list
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
        Glide.with(context).load(data.getDataImage()).into(holder.recImage);
        holder.recTitle.setText(data.getDataTitle() != null ? data.getDataTitle() : "");
        holder.recDesc.setText(data.getDataDesc() != null ? data.getDataDesc() : "");
        holder.recLang.setText(data.getDataLang() != null ? data.getDataLang() : "");

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

    // Use DiffUtil for efficient list updates
    public void searchDataList(ArrayList<DataClass> searchList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return dataList.size();
            }

            @Override
            public int getNewListSize() {
                return searchList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return dataList.get(oldItemPosition).getKey()
                        .equals(searchList.get(newItemPosition).getKey());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                DataClass oldData = dataList.get(oldItemPosition);
                DataClass newData = searchList.get(newItemPosition);
                return oldData.equals(newData);
            }
        });
        this.dataList.clear(); // Clear the existing list
        this.dataList.addAll(searchList); // Add the new filtered list
        diffResult.dispatchUpdatesTo(this);
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
