package com.kutub.sqlitedatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kutub.sqlitedatabase.databasehelper.DatabaseHelper;
import com.kutub.sqlitedatabase.model.Student;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editTextName, editTextEmail, editTextId;
    Button btnShow, btnAdd, btnUpdate, btnDelete;
    TextView textViewResult;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this, null, null, 1);

        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnShow = findViewById(R.id.btnShow);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                Student student = new Student(name, email);
                boolean inserted = db.createStudent(student);

                if (inserted) {
                    Toast.makeText(MainActivity.this, "Student Add Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Student Add Unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Student> students = db.getAllStudent();
                if (students.size() == 0) {
                    textViewResult.setText("Student  Not  Found");
                    return;
                }

                StringBuilder builder = new StringBuilder();
                for (Student student : students) {
                    builder.append("ID : ").append(student.getId()).append("\n");
                    builder.append("NAME : ").append(student.getId()).append("\n");
                    builder.append("EMAIL : ").append(student.getId()).append("\n\n");

                    textViewResult.setText(builder.toString());
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editTextId.getText().toString();
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                Student student = new Student(Integer.parseInt(id), name, email);
                boolean isUpdate = db.updateStudent(student);
                if (isUpdate) {
                    Toast.makeText(MainActivity.this, "Student Update Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Student Update Unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editTextId.getText().toString();

                boolean isDelete = db.deleteStudent(Integer.parseInt(id));
                if (isDelete) {
                    Toast.makeText(MainActivity.this, "Student Delete Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Student Delete Unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}