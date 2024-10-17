package com.kutub.studentapp;

import android.content.Intent;
import android.net.Uri; // Add this import for opening the dial pad
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView detailName, detailRegNo, detailAge, detailGender, detailContact, detailParent;
    ImageView detailImage;

    Button deleteBtn, editBtn;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailName = findViewById(R.id.detailName);
        detailRegNo = findViewById(R.id.detialRegNo);
        detailAge = findViewById(R.id.detialAge);
        detailGender = findViewById(R.id.detialGender);
        detailContact = findViewById(R.id.detialContact);
        detailParent = findViewById(R.id.detialParentNo);
        detailImage = findViewById(R.id.detailImage);
        deleteBtn = findViewById(R.id.deleteBtn);
        editBtn = findViewById(R.id.editBtn);

        // Extracting data from bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailName.setText(bundle.getString("Name"));
            detailRegNo.setText(bundle.getString("RegNo"));
            detailAge.setText(String.valueOf(bundle.getInt("Age"))); // Convert to String
            detailGender.setText(bundle.getString("Gender"));
            detailContact.setText(String.valueOf(bundle.getInt("ContactNo"))); // Convert to String
            detailParent.setText(String.valueOf(bundle.getInt("ParentNo"))); // Convert to String
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

        // Delete button functionality
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainViewActivity.class));
                        finish();
                    }
                });
            }
        });

        // Edit button functionality
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Name", detailName.getText().toString())
                        .putExtra("RegNo", detailRegNo.getText().toString())
                        .putExtra("Age", Integer.parseInt(detailAge.getText().toString()))
                        .putExtra("Gender", detailGender.getText().toString())
                        .putExtra("ContactNo", Integer.parseInt(detailContact.getText().toString()))
                        .putExtra("ParentNo", Integer.parseInt(detailParent.getText().toString()))
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);

                startActivity(intent);
            }
        });

        // Add click listeners to open dialer on contact number and parent number click
        detailContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactNumber = detailContact.getText().toString().trim();
                openDialPad(contactNumber);
            }
        });

        detailParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parentNumber = detailParent.getText().toString().trim();
                openDialPad(parentNumber);
            }
        });
    }

    // Method to open dialer with a phone number
    private void openDialPad(String phoneNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(dialIntent);
    }
}
