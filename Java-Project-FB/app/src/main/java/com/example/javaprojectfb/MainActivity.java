package com.example.javaprojectfb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText editTextId, editTextName;
    private Button btnSave, btnShow, btnUpdate, btnDelete;

    private TextView textViewId, textViewName;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        textViewId = findViewById(R.id.textViewId);
        textViewName = findViewById(R.id.textViewName);
        btnSave = findViewById(R.id.btnSave);
        btnShow = findViewById(R.id.btnShow);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("MyDB");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = Integer.parseInt(editTextId.getText().toString());
                String name = editTextName.getText().toString();
                HashMap hashMap=new HashMap();
                hashMap.put("id",id);
                hashMap.put("name",name);

                databaseReference.child("user1").setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Data Add Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Data Add Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String showdata = snapshot.getValue().toString();
                            textViewName.setText(showdata);
                        } else {
                            Toast.makeText(MainActivity.this, "No Data ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}