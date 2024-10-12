package com.kutubuddin.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.util.Calendar;

public class UploadActivity extends AppCompatActivity {

    Button saveButton;
    EditText uploadTopic, uploadDesc, uploadLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Initialize views
        uploadDesc = findViewById(R.id.uploadDesc);
        uploadTopic = findViewById(R.id.uploadTopic);
        uploadLang = findViewById(R.id.uploadLang);
        saveButton = findViewById(R.id.saveButton);

        // Save button onClickListener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    // Save data
    public void saveData() {
        // Get the input from fields
        String title = uploadTopic.getText().toString().trim();
        String desc = uploadDesc.getText().toString().trim();
        String lang = uploadLang.getText().toString().trim();

        // Validate if all fields are filled
        if (title.isEmpty() || desc.isEmpty() || lang.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout); // Ensure you have a layout for progress
        AlertDialog dialog = builder.create();
        dialog.show();

        // Upload data to Firebase Realtime Database
        uploadData(dialog);
    }

    // Save data to Firebase Realtime Database
    public void uploadData(AlertDialog dialog) {
        String title = uploadTopic.getText().toString().trim();
        String desc = uploadDesc.getText().toString().trim();
        String lang = uploadLang.getText().toString().trim();

        // Create DataClass object (without imageURL)
        DataClass dataClass = new DataClass(title, desc, lang);

        // Generate a unique key based on the current date and time
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        // Save data to Firebase Database under "Android Tutorials"
        FirebaseDatabase.getInstance().getReference("Android Tutorials").child(currentDate)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Success message and finish activity
                            Toast.makeText(UploadActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                            finish();  // Close the activity
                        } else {
                            // Handle any failure in the database update process
                            Toast.makeText(UploadActivity.this, "Data Save Failed", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show error message
                        Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }
}
