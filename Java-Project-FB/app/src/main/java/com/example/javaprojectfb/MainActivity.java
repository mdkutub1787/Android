package com.example.javaprojectfb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText editTextId, editTextName;
    private Button btnSave, btnShow, btnUpdate, btnDelete;
    private TextView textViewId, textViewName;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing UI elements
        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        textViewId = findViewById(R.id.textViewId);
        textViewName = findViewById(R.id.textViewName);
        btnSave = findViewById(R.id.btnSave);
        btnShow = findViewById(R.id.btnShow);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Setting up Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MyDB");

        // Save Button Functionality
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editTextId.getText().toString());
                String name = editTextName.getText().toString();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", id);
                hashMap.put("name", name);

                databaseReference.child(String.valueOf(id)).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Data Addition Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Show Button Functionality
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editTextId.getText().toString());
                databaseReference.child(String.valueOf(id)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                            assert map != null;
                            Object idValue = map.get("id");
                            String name = Objects.requireNonNull(map.get("name")).toString();

                            textViewId.setText(String.valueOf(idValue));
                            textViewName.setText(name);
                        } else {
                            Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Failed to Retrieve Data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Update Button Functionality
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editTextId.getText().toString());
                String name = editTextName.getText().toString();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", id);
                hashMap.put("name", name);

                databaseReference.child(String.valueOf(id)).updateChildren(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Data Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Delete Button Functionality
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editTextId.getText().toString());
                databaseReference.child(String.valueOf(id)).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Data Deletion Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
