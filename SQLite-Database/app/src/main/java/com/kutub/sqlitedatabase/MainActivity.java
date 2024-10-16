package com.kutub.sqlitedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kutub.sqlitedatabase.model.Student;
import com.kutub.sqlitedatabase.database.DatabaseHelper; // Assuming you have a DatabaseHelper class for DB operations

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextId;
    private Button btnShow, btnAdd, btnUpdate, btnDelete;
    private TextView textViewResult;
    private DatabaseHelper db; // Database helper instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextId = findViewById(R.id.editTextId);
        btnShow = findViewById(R.id.btnShow);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        textViewResult = findViewById(R.id.textViewResult);

        // Initialize the database helper
        db = new DatabaseHelper(this);

        // Set button click listeners
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStudents();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });
    }

    private void showStudents() {
        List<Student> students = db.getAllStudents();
        if (students.size() == 0) {
            Toast.makeText(MainActivity.this, "No Students Found", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (Student student : students) {
            builder.append("ID: ").append(student.getId()).append("\n");
            builder.append("NAME: ").append(student.getName()).append("\n");
            builder.append("EMAIL: ").append(student.getEmail()).append("\n\n");
        }

        Intent intent = new Intent(MainActivity.this, ShowStudentsActivity.class);
        intent.putExtra("students_data", builder.toString());
        startActivity(intent);
    }

    private void addStudent() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Student student = new Student(name, email);
        db.addStudent(student);
        Toast.makeText(this, "Student Added", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void updateStudent() {
        String idStr = editTextId.getText().toString();
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        if (idStr.isEmpty() || name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        int id = Integer.parseInt(idStr);
        Student student = new Student(id, name, email);
        db.updateStudent(student);
        Toast.makeText(this, "Student Updated", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void deleteStudent() {
        String idStr = editTextId.getText().toString();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Please enter ID to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        int id = Integer.parseInt(idStr);
        db.deleteStudent(id);
        Toast.makeText(this, "Student Deleted", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void clearFields() {
        editTextName.setText("");
        editTextEmail.setText("");
        editTextId.setText("");
    }
}
