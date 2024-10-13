package com.kutub.insurancecrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PolicyDetail extends AppCompatActivity {

    private TextView id, date, bankName, policyHolder, address, stockItem, sumInsurd;
    FloatingActionButton deleteButton, editButton;
    String key = ""; // Key to identify the policy in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_detail);

        // Initialize views
        id = findViewById(R.id.id);
        date = findViewById(R.id.date);
        bankName = findViewById(R.id.bankName);
        policyHolder = findViewById(R.id.policyHolder);
        address = findViewById(R.id.address);
        stockItem = findViewById(R.id.stockItem);
        sumInsurd = findViewById(R.id.sumInsurd);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        // Retrieve data from the intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id.setText(bundle.getString("ID", ""));
            date.setText(bundle.getString("Date", ""));
            bankName.setText(bundle.getString("BankName", ""));
            policyHolder.setText(bundle.getString("PolicyHolder", ""));
            address.setText(bundle.getString("Address", ""));
            stockItem.setText(bundle.getString("StockItem", ""));
            sumInsurd.setText(bundle.getString("SumInsurd", ""));
            key = bundle.getString("Key", ""); // Store the key for deletion
        }

        // Set delete button action
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePolicy();
            }
        });

        // Set edit button action
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPolicy();
            }
        });
    }

    // Delete policy from the database
    private void deletePolicy() {
        if (!key.isEmpty()) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Policies"); // Assuming "Policies" is the node name
            reference.child(key).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(PolicyDetail.this, "Policy Deleted Successfully", Toast.LENGTH_SHORT).show();
                            // Redirect to the main activity after deletion
                            startActivity(new Intent(PolicyDetail.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PolicyDetail.this, "Failed to delete policy", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(PolicyDetail.this, "Invalid key", Toast.LENGTH_SHORT).show();
        }
    }

    // Edit policy details
    private void editPolicy() {
        Intent intent = new Intent(PolicyDetail.this,PolicyUpdate.class); // Assuming EditPolicyActivity is the activity for editing policies
        intent.putExtra("ID", id.getText().toString());
        intent.putExtra("Date", date.getText().toString());
        intent.putExtra("BankName", bankName.getText().toString());
        intent.putExtra("PolicyHolder", policyHolder.getText().toString());
        intent.putExtra("Address", address.getText().toString());
        intent.putExtra("StockItem", stockItem.getText().toString());
        intent.putExtra("SumInsurd", sumInsurd.getText().toString());
        intent.putExtra("Key", key);
        startActivity(intent);
    }
}
