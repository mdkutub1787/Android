package com.kutub.insurancecrud;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import com.kutub.insurancecrud.model.PolicyModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PolicySave extends AppCompatActivity {

    private int nextPolicyId;
    private Button saveButton;
    private EditText id, date, bankName, policyHolder, address, stockItem, sumInsured;

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_policysave);

        saveButton = findViewById(R.id.saveButton);
        id = findViewById(R.id.id);
        date = findViewById(R.id.date);
        bankName = findViewById(R.id.bankName);
        policyHolder = findViewById(R.id.policyHolder);
        address = findViewById(R.id.address);
        stockItem = findViewById(R.id.stockItem);
        sumInsured = findViewById(R.id.sumInsurd);

        fetchNextPolicyId();
        setCurrentDate();

        // Set OnClickListener for date EditText
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePolicyData();
            }
        });
    }

    private void fetchNextPolicyId() {
        DatabaseReference policiesRef = FirebaseDatabase.getInstance().getReference("Policies");
        policiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nextPolicyId = (int) dataSnapshot.getChildrenCount() + 1; // Incrementing for new ID
                id.setText(String.valueOf(nextPolicyId));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PolicySave.this, "Failed to fetch ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePolicyData() {
        String policyId = id.getText().toString().trim();
        String bank = bankName.getText().toString().trim();
        String holder = policyHolder.getText().toString().trim();
        String addr = address.getText().toString().trim();
        String stock = stockItem.getText().toString().trim();
        String sum = sumInsured.getText().toString().trim();
        String dateValue = date.getText().toString().trim();

        if (!validateFields(policyId, bank, holder, addr, stock, sum) || !isValidDate(dateValue)) {
            Toast.makeText(this, "Please fill all fields with valid data", Toast.LENGTH_SHORT).show();
            return;
        }

        int insuredSum;
        try {
            insuredSum = Integer.parseInt(sum);  // Parse the sum insured as int
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid sum insured", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(PolicySave.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Corrected order and parameter types for PolicyModel constructor
        PolicyModel policyData = new PolicyModel(nextPolicyId, dateValue, bank, holder, addr, stock, insuredSum);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault());
        String currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("Policies").child(currentDate)
                .setValue(policyData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PolicySave.this, "Policy Data Saved Successfully", Toast.LENGTH_SHORT).show();
                            clearFields();
                            finish();
                        } else {
                            Toast.makeText(PolicySave.this, "Policy save failed", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PolicySave.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }

    private void clearFields() {
        bankName.setText("");
        policyHolder.setText("");
        address.setText("");
        stockItem.setText("");
        sumInsured.setText("");
        fetchNextPolicyId();
        setCurrentDate();
    }

    private boolean validateFields(String... fields) {
        for (String field : fields) {
            if (TextUtils.isEmpty(field)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void setCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
        date.setText(formattedDate);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(PolicySave.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        date.setText(formattedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}
