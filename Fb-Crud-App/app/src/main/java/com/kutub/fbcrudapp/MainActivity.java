package com.kutub.fbcrudapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;
import com.kutub.fbcrudapp.adapter.MyAdapter;
import com.kutub.fbcrudapp.model.DataClass;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    SearchView searchView;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Set up progress dialog for loading
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        // Initialize the adapter with the empty data list
        adapter = new MyAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials");
        fetchAllData(); // Initial data fetch

        // Search query listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // We handle filtering in real-time
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText); // Filter list as text is typed
                return true;
            }
        });

        // Floating action button click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to fetch all data from Firebase and populate the list
    private void fetchAllData() {
        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss(); // Close progress dialog on failure
            }
        });
    }

    // Search method to filter the list based on user input
    public void searchList(String text) {
        Query query = databaseReference.orderByChild("dataTitle")
                .startAt(text)
                .endAt(text + "\uf8ff");

        dialog.show(); // Show loading dialog while searching
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss(); // Dismiss loading dialog after search is done
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss(); // Dismiss loading dialog on failure
            }
        });
    }

    // Remove Firebase event listener to prevent memory leaks
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseReference != null && eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
}
