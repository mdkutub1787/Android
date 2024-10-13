package com.kutubuddin.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import androidx.annotation.NonNull;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize views
        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detailLang = findViewById(R.id.detailLang);

        // Retrieve data from intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description", ""));
            detailTitle.setText(bundle.getString("Title", ""));
            detailLang.setText(bundle.getString("Language", ""));
            key = bundle.getString("Key", "");
            imageUrl = bundle.getString("Image", "");

            // Use Glide to load the image with placeholders and error handling
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.error_image)  // Replace with actual placeholder image
                    .error(R.drawable.error_image)        // Replace with actual error image
                    .into(detailImage);
        }

        // Set delete button action
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem();
            }
        });

        // Set edit button action
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem();
            }
        });
    }

    // Delete item function
    private void deleteItem() {
        if (key != null && !key.isEmpty()) {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Tutorials");
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

            // Delete the image from Firebase Storage
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    // Delete the database record
                    reference.child(key).removeValue();
                    Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error handling
                    Toast.makeText(DetailActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DetailActivity.this, "Invalid key or image URL", Toast.LENGTH_SHORT).show();
        }
    }

    // Edit item function
    private void editItem() {
        Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
        intent.putExtra("Title", detailTitle.getText().toString());
        intent.putExtra("Description", detailDesc.getText().toString());
        intent.putExtra("Language", detailLang.getText().toString());
        intent.putExtra("Image", imageUrl);
        intent.putExtra("Key", key);
        startActivity(intent);
    }
}
