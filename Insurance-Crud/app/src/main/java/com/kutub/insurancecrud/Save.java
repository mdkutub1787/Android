package com.kutub.insurancecrud;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import com.kutub.insurancecrud.model.InsuranceModel;
import java.text.DateFormat;
import java.util.Calendar;

public class Save extends AppCompatActivity {

    // Declare UI elements
    Button saveButton;
    EditText id, bankName, policyHolder, address, stockItem, sumInsurd;
    private int nextPolicyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout
        EdgeToEdge.enable(this);

        // Set the layout for this activity
        setContentView(R.layout.activity_save);

        // Initialize UI elements
        saveButton = findViewById(R.id.saveButton);
        id = findViewById(R.id.id);
        bankName = findViewById(R.id.bankName);
        policyHolder = findViewById(R.id.policyHolder);
        address = findViewById(R.id.address);
        stockItem = findViewById(R.id.stockItem);
        sumInsurd = findViewById(R.id.sumInsurd);

        // Fetch the current max ID and set the next one
        fetchNextPolicyId();

        // Save button onClickListener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePolicyData();
            }
        });
    }

    // Method to fetch the next policy ID
    private void fetchNextPolicyId() {
        DatabaseReference policiesRef = FirebaseDatabase.getInstance().getReference("Policies");
        policiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nextPolicyId = (int) dataSnapshot.getChildrenCount() + 1; // Incrementing for new ID
                id.setText(String.valueOf(nextPolicyId)); // Set ID field with next policy ID
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Save.this, "Failed to fetch ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to handle saving the data
    private void savePolicyData() {
        // Get values from fields
        String policyId = id.getText().toString().trim();
        String bank = bankName.getText().toString().trim();
        String holder = policyHolder.getText().toString().trim();
        String addr = address.getText().toString().trim();
        String stock = stockItem.getText().toString().trim();
        String sum = sumInsurd.getText().toString().trim();

        // Data validation: Check if any field is empty
        if (policyId.isEmpty() || bank.isEmpty() || holder.isEmpty() ||
                addr.isEmpty() || stock.isEmpty() || sum.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert sumInsured from String to int
        int insuredSum;
        try {
            insuredSum = Integer.parseInt(sum); // Convert sum to int
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid sum insured", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Save.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout); // Ensure you have a layout for progress
        AlertDialog dialog = builder.create();
        dialog.show();

        // Create a PolicyDataClass object with the collected data
        InsuranceModel policyData = new InsuranceModel(nextPolicyId, bank, holder, addr, stock, insuredSum, "");

        // Generate a unique key based on the current date and time
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        // Save data to Firebase Database under "Policies"
        FirebaseDatabase.getInstance().getReference("Policies").child(currentDate)
                .setValue(policyData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Success message and finish activity
                            Toast.makeText(Save.this, "Policy Data Saved Successfully", Toast.LENGTH_SHORT).show();
                            clearFields();  // Clear input fields after successful save
                            finish();  // Close the activity
                        } else {
                            // Handle any failure in the database update process
                            Toast.makeText(Save.this, "Data Save Failed", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss(); // Dismiss dialog after operation
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show error message
                        Toast.makeText(Save.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss(); // Dismiss dialog on error
                    }
                });
    }

    // Method to clear the input fields after saving
    private void clearFields() {
        bankName.setText("");
        policyHolder.setText("");
        address.setText("");
        stockItem.setText("");
        sumInsurd.setText("");
        fetchNextPolicyId(); // Reset ID for the next entry
    }
}
