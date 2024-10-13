//package com.kutub.insurancecrud;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.SearchView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//import com.kutub.insurancecrud.adapter.PolicyAdapter;
//import com.kutub.insurancecrud.model.PolicyModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PolicyRecycler extends AppCompatActivity {
//
//    FloatingActionButton fab;
//    DatabaseReference databaseReference;
//    ValueEventListener eventListener;
//    RecyclerView recyclerView;
//    List<PolicyModel> dataList;
//    PolicyAdapter policyAdapter;
//    SearchView searchView;
//    AlertDialog dialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_policy_recycler);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        fab = findViewById(R.id.search);
//        searchView = findViewById(R.id.search);
//        searchView.clearFocus();
//
//        // Set up RecyclerView with GridLayoutManager
//        recyclerView.setLayoutManager(new GridLayoutManager(PolicyRecycler.this, 1));
//
//        // Set up progress dialog for loading
//        AlertDialog.Builder builder = new AlertDialog.Builder(PolicyRecycler.this);
//        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
//        dialog = builder.create();
//        dialog.show();
//
//        dataList = new ArrayList<>();
//        policyAdapter = new PolicyAdapter(PolicyRecycler.this, dataList);
//        recyclerView.setAdapter(policyAdapter);
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials");
//        fetchAllData(); // Initial data fetch
//
//        // Search query listener
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false; // No action needed on submit
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchList(newText.toLowerCase()); // Normalize text for search
//                return true;
//            }
//        });
//
//        fab.setOnClickListener(view -> {
//            Intent intent = new Intent(PolicyRecycler.this, PolicySave.class);
//            startActivity(intent);
//        });
//    }
//
//    // Method to fetch all data from Firebase and populate the list
//    private void fetchAllData() {
//        dialog.show();
//        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                dataList.clear();
//                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
//                    PolicyModel dataClass = itemSnapshot.getValue(PolicyModel.class);
//                    if (dataClass != null) {
//                        // Assuming you have a method setId in PolicyModel
//                        dataClass.setId(Integer.parseInt(itemSnapshot.getKey())); // Adjust this if necessary
//                        dataList.add(dataClass);
//                    } else {
//                        Log.w("FirebaseWarning", "DataClass is null for key: " + itemSnapshot.getKey());
//                    }
//                }
//                policyAdapter.notifyDataSetChanged();
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                dialog.dismiss(); // Close progress dialog on failure
//                Toast.makeText(PolicyRecycler.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("FirebaseError", "Error: " + error.getMessage());
//            }
//        });
//    }
//
//    // Search method to filter the list based on user input
//    public void searchList(String text) {
//        Query query = databaseReference.orderByChild("dataTitle") // Ensure this matches your Firebase data structure
//                .startAt(text)
//                .endAt(text + "\uf8ff");
//
//        dialog.show(); // Show loading dialog while searching
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                dataList.clear();
//                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
//                    PolicyModel dataClass = itemSnapshot.getValue(PolicyModel.class);
//                    if (dataClass != null) { // Check for null values
//                        dataClass.setId(Integer.parseInt(itemSnapshot.getKey())); // Ensure this is correct
//                        dataList.add(dataClass);
//                    }
//                }
//                policyAdapter.notifyDataSetChanged();
//                dialog.dismiss(); // Dismiss loading dialog after search is done
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                dialog.dismiss(); // Dismiss loading dialog on failure
//                Toast.makeText(PolicyRecycler.this, "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("FirebaseError", "Search Error: " + error.getMessage());
//            }
//        });
//    }
//
//    // Remove Firebase event listener to prevent memory leaks
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (databaseReference != null && eventListener != null) {
//            databaseReference.removeEventListener(eventListener);
//        }
//    }
//}
