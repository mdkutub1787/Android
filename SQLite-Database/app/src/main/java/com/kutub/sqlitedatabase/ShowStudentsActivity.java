package com.kutub.sqlitedatabase;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowStudentsActivity extends AppCompatActivity {

    private TextView textViewStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        textViewStudents = findViewById(R.id.textViewStudents);
        String studentsData = getIntent().getStringExtra("students_data");
        textViewStudents.setText(studentsData);
    }
}
