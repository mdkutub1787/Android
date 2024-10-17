package com.kutub.studentapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updateRegNo, updateName, updateAge, updateContact, updateParent;
    RadioGroup genderGroup;
    RadioButton radioMale, radioFemale;
    String imageUrl, regNo, name, gender, key, oldImageUrl;
    Integer age, contactNumber, parentNumber;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Initialize UI components
        updateImage = findViewById(R.id.updateImage);
        updateButton = findViewById(R.id.updateButton);
        updateRegNo = findViewById(R.id.updateStdRegNo);
        updateName = findViewById(R.id.updateStdName);
        updateAge = findViewById(R.id.updateStdAge);
        updateContact = findViewById(R.id.updateStdMobNo);
        updateParent = findViewById(R.id.updateStdParentNo);

        genderGroup = findViewById(R.id.genderGroup);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        // Set up Activity Result Launcher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                            storageReference = FirebaseStorage.getInstance().getReference().child("Images").child(uri.getLastPathSegment());
                        } else {
                            Toast.makeText(UpdateActivity.this, "No Image Selected!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Populate the form with data from the intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            updateName.setText(bundle.getString("Name"));
            updateRegNo.setText(bundle.getString("RegNo"));
            updateAge.setText(String.valueOf(bundle.getInt("Age")));
            updateContact.setText(String.valueOf(bundle.getInt("ContactNo")));
            updateParent.setText(String.valueOf(bundle.getInt("ParentNo")));
            key = bundle.getString("Key");
            oldImageUrl = bundle.getString("Image");

            // Set the gender RadioButton based on passed data
            String genderValue = bundle.getString("Gender");
            if (genderValue.equals("Male")) {
                radioMale.setChecked(true);
            } else if (genderValue.equals("Female")) {
                radioFemale.setChecked(true);
            }

            Glide.with(UpdateActivity.this)
                    .load(oldImageUrl)
                    .into(updateImage);
        }

        // Reference to the student in Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Students").child(key);

        // Image Click Listener
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        // Update Button Click Listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(UpdateActivity.this, MainViewActivity.class);
                startActivity(intent);
            }
        });
    }

    // Save Data to Firebase
    public void saveData() {
        if (uri != null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Images").child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    updateData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        } else {
            imageUrl = oldImageUrl;
            updateData();
        }
    }

    // Update Data in Firebase
    public void updateData() {
        name = updateName.getText().toString().trim();
        regNo = updateRegNo.getText().toString().trim();
        age = Integer.valueOf(updateAge.getText().toString());

        // Get selected gender from RadioGroup
        int selectedGenderId = genderGroup.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.radioMale) {
            gender = "Male";
        } else if (selectedGenderId == R.id.radioFemale) {
            gender = "Female";
        }

        contactNumber = Integer.valueOf(updateContact.getText().toString());
        parentNumber = Integer.valueOf(updateParent.getText().toString());

        DataClass dataClass = new DataClass(regNo, name, age, gender, contactNumber, parentNumber, imageUrl);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Delete the old image after updating
                    int lastSlashIndex = oldImageUrl.lastIndexOf('/');
                    String path = oldImageUrl.substring(lastSlashIndex + 1);
                    StorageReference reference = FirebaseStorage.getInstance().getReference().child(path);
                    reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UpdateActivity.this, "Student Update Successfully !!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateActivity.this, "Updated! (Image deletion failed)", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
